package com.gradproj.optibodyai.chat_gpt_activity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gradproj.optibodyai.R
import com.gradproj.optibodyai.ui.theme.OptibodyAITheme
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import org.json.JSONArray




val ActiveColor = Color(0xff7dde86)

@Composable
fun ChatGPTScreen() {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
                .imePadding()
                .padding(paddingValues)
        ) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                Header()
                Spacer(modifier = Modifier.height(32.dp))
                val messages = remember {
                    mutableStateListOf("Hello, I’m ChatGPT! 👋 I’m your personal fitness assistant. How can I help you?")
                }

                Messages(messages)
                Spacer(modifier = Modifier.height(32.dp))
                SendMessage(
                    onSend = { userMessage ->
                        messages.add(userMessage)
                    },
                    onChatResponse = { chatResponse ->
                        messages.add(chatResponse)
                    }
                )
            }
        }
    }
}


@Composable
private fun ColumnScope.Messages(messages: SnapshotStateList<String>) {
    val listState = rememberLazyListState() // Create LazyListState to track the scroll position

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size - 1)
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .weight(1f),
        state = listState
    ) {
        itemsIndexed(messages) { index, item ->
            Row(horizontalArrangement = Arrangement.End) {
                if (index % 2 == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
                Row(
                    Modifier.fillMaxWidth(0.85f),
                    horizontalArrangement = if (index % 2 == 1) Arrangement.End else Arrangement.Start
                ) {
                    if (index % 2 == 0) {
                        Icon(
                            painter = painterResource(id = R.drawable.robot),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                    Card(
                        Modifier.padding(start = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (index % 2 == 0) Color.White else MaterialTheme.colorScheme.primary,
                        ),
                        shape = RoundedCornerShape(
                            if (index % 2 == 0) 0 else 24,
                            if (index % 2 == 0) 24 else 0,
                            24,
                            24
                        )
                    ) {
                        Text(text = item, modifier = Modifier.padding(16.dp), color =if (index % 2 == 0) Color.Black else Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SendMessage(
    onSend: (String) -> Unit,
    onChatResponse: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.weight(1f)) {
            BottomOutlineTextField(
                placeholder = "Type a message...",
                value = text
            ) {
                text = it
            }
        }
        Spacer(modifier = Modifier.width(6.dp))

        IconButton(
            onClick = {
                onSend(text) // Adds the user's message to the chat
                val userMessage = text
                text = "" // Clear input field

                // Call ChatGPT API
                sendChatGPTMessage(userMessage,
                    onResponse = { chatResponse ->
                        onChatResponse(chatResponse) // Adds ChatGPT's response to the chat
                    },
                    onError = { errorMessage ->
                        onChatResponse("Error: $errorMessage") // Display error in chat if API fails
                    }
                )
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.send),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}




@Composable
fun BottomOutlineTextField(placeholder: String, value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
            .padding(1.dp)
    ) {
        BasicTextField(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 18.dp)
                .fillMaxSize(),
            cursorBrush = Brush.verticalGradient( // Change the cursor color here
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary
                ) // Single or gradient color
            ),
            value = value,
            singleLine = true,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 20.sp,
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
//                    .background(Color.Blue)
                        .fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.weight(1f)) { innerTextField() }
                        IconButton(
//                            modifier = Modifier.padding(start = 8.dp),
                            onClick = { /*TODO*/ },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.mic),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun Header() {
    Row(verticalAlignment = Alignment.CenterVertically) {

        val context = LocalContext.current as ChatGPTActivity

        OutlinedIconButton(
            onClick = {
                context.finish()
            },
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            painter = painterResource(id = R.drawable.robot),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = "ChatGPT", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ActiveColor)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Always active",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        OutlinedIconButton(
            onClick = { /*TODO*/ },
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                modifier = Modifier.rotate(90f),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Preview
@Composable
fun ChatGPTPreview() {
    OptibodyAITheme {
        ChatGPTScreen()
    }
}
fun sendChatGPTMessage(message: String, onResponse: (String) -> Unit, onError: (String) -> Unit) {
    val client = OkHttpClient()
    val apiKey = "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" // Replace with your OpenAI API key
    val url = "https://api.openai.com/v1/chat/completions"

    // Create JSON body
    val jsonBody = JSONObject()
    jsonBody.put("model", "gpt-3.5-turbo")
    jsonBody.put("messages", JSONArray().apply {
        put(JSONObject().apply {
            put("role", "user")
            put("content", message)
        })
    })

    val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())

    // Build the request
    val request = Request.Builder()
        .url(url)
        .addHeader("Authorization", "Bearer $apiKey")
        .post(requestBody)
        .build()

    // Make asynchronous network request
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onError("Failed to reach ChatGPT API: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                response.body?.let { responseBody ->
                    val jsonResponse = JSONObject(responseBody.string())
                    val replyMessage = jsonResponse
                        .getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                    onResponse(replyMessage)
                } ?: onError("Received empty response from ChatGPT.")
            } else {
                onError("Request failed: ${response.message}")
            }
        }
    })
}