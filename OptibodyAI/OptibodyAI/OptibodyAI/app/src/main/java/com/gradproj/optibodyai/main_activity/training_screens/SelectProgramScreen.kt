package com.gradproj.optibodyai.main_activity.training_screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gradproj.optibodyai.R
import com.gradproj.optibodyai.components.LoadingCircle
import com.gradproj.optibodyai.components.ScreenHeader
import com.gradproj.optibodyai.ui.theme.OptibodyAITheme
import com.gradproj.optibodyai.ui.theme.Rounded16
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally


import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.gradproj.optibodyai.components.ScreenHeader
import com.gradproj.optibodyai.components.TextFieldComponent
import com.gradproj.optibodyai.supabase
import com.gradproj.optibodyai.ui.theme.Secondary
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import io.github.jan.supabase.postgrest.query.Columns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gradproj.optibodyai.ui.theme.Secondary
import com.google.gson.Gson
import kotlinx.coroutines.withContext
import kotlinx.coroutines.runBlocking
import com.google.gson.reflect.TypeToken
import io.github.jan.supabase.SupabaseClient // For initializing Supabase client
import io.github.jan.supabase.postgrest.query.PostgrestQueryBuilder // To use eq and other filters
import kotlinx.coroutines.runBlocking // For coroutine scope
import kotlinx.serialization.decodeFromString // JSON decoding (if needed)
import kotlinx.coroutines.runBlocking
import io.github.jan.supabase.postgrest.postgrest

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.ui.platform.LocalContext
import com.google.gson.GsonBuilder
import io.ktor.http.ContentDisposition.Companion.File
import kotlinx.io.IOException
import java.io.File
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle


val Levels = listOf(
    "Beginner",
    "Intermediate",
    "Advanced"
)

@Composable
fun SelectProgramScreen(navController: NavHostController) {
    val fitness_level = remember { mutableStateOf("") }
    val has_injury_back = remember { mutableStateOf(false) }
    val has_injury_knee = remember { mutableStateOf(false) }
    val has_injury_shoulder = remember { mutableStateOf(false) }
    val recommendations = remember { mutableStateOf<List<Recommendation>>(emptyList()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val userId = supabase.auth.currentSessionOrNull()?.user?.id
                Log.d("TrainingScreen", "Fetching data for user ID: $userId")
                val userData = fetchUserData2(
                    supabaseClient = supabase,
                    userId = userId ?: throw Exception("User ID not found")
                )
                withContext(Dispatchers.Main) {
                    fitness_level.value = userData?.fitness_level ?: "Unknown"
                    Log.d("TrainingScreen", "Fitness updated: ${fitness_level.value}")

                    has_injury_back.value = userData?.has_injury_back ?: false
                    Log.d("TrainingScreen", "Back updated: ${has_injury_back.value}")

                    has_injury_knee.value = userData?.has_injury_knee ?: false
                    Log.d("TrainingScreen", "Knee updated: ${has_injury_knee.value}")

                    has_injury_shoulder.value = userData?.has_injury_shoulder ?: false
                    Log.d("TrainingScreen", "Shoulder updated: ${has_injury_shoulder.value}")


                }
            } catch (e: Exception) {
                Log.e("TrainingScreen", "Error fetching user data: ${e.message}")
                withContext(Dispatchers.Main) {
                    fitness_level.value = "Error loading data"
                    has_injury_back.value = false
                    has_injury_knee.value = false
                    has_injury_shoulder.value = false

                }
            }
        }
    }

    val injuries = mutableListOf<String>()
    if (has_injury_back.value) injuries.add("Back")
    if (has_injury_knee.value) injuries.add("Knee")
    if (has_injury_shoulder.value) injuries.add("Shoulder")
    Log.d("TrainingScreen", "The list: ${injuries}")
    val equipment = listOf("Dumbbell", "Body Weight", "Barbell", "Kettlebell", "Resistance Band", "Machine", "Cable", "Medicine Ball")
    val difficulty = fitness_level.value.toIntOrNull() ?: 1 // Default to 1 if invalid
    Log.d("TrainingScreen", "The list: ${difficulty}")

    val payload = mapOf(
        "difficulty_range" to listOf(difficulty, difficulty), // Example range, adjust as needed
        "equipment" to equipment,
        "injuries" to injuries
    )


    val client = OkHttpClient()

    val gson = GsonBuilder()
        .setFieldNamingStrategy { field ->
            // Custom mapping: Replace camelCase with JSON-style keys
            when (field.name) {
                "exerciseName" -> "Exercise Name"
                "mainMuscle" -> "Main_muscle"
                "mechanics" -> "Mechanics"
                "suitability" -> "Suitability"
                else -> field.name // Fallback to default name
            }
        }
        .create()
    val jsonPayload = gson.toJson(payload) // Convert map to JSON string
    val requestBody = jsonPayload.toRequestBody("application/json; charset=utf-8".toMediaType())
    val request = Request.Builder()
        .url("http://192.168.101.2:8080/recommend") // Use your PC's IP address
        .post(requestBody)
        .build()










    Column(
        Modifier.padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        ScreenHeader(title = "Get ready \nfor today’s training 🔥")

        Spacer(modifier = Modifier.height(64.dp))

        // Level Selection
        var selectedLevel: String by remember { mutableStateOf(Levels[1]) } // Default to Intermediate

        Text(
            text = "Weight Lifting Program",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
            for (level in Levels) {
                val backgroundColor by animateColorAsState(
                    targetValue = if (level == selectedLevel) MaterialTheme.colorScheme.secondary else Color.Transparent,
                    label = ""
                )
                Levels(level, backgroundColor, level == selectedLevel) {
                    Log.d("TrainingScreen", "Selected level: $it")
                    selectedLevel = it // Update selected level

                    // Adjust the difficulty range based on the selected level
                    val difficultyRange = when (selectedLevel) {
                        "Beginner" -> listOf(difficulty - 1, difficulty)
                        "Intermediate" -> listOf(difficulty, difficulty)
                        "Advanced" -> listOf(difficulty, difficulty + 1)
                        else -> listOf(1, 5) // Default range
                    }
                    Log.d("TrainingScreen", "Difficulty range for $selectedLevel: $difficultyRange")

                    // Fetch Recommendations
                    val payload = mapOf(
                        "difficulty_range" to difficultyRange,
                        "equipment" to listOf(
                            "Dumbbell", "Body Weight", "Barbell", "Kettlebell", "Resistance Band",
                            "Machine", "Cable", "Medicine Ball"
                        ),
                        "injuries" to injuries
                    )

                    Log.d("TrainingScreen", "Payload created: $payload")
                    val client = OkHttpClient()

                    val gson = GsonBuilder()
                        .setFieldNamingStrategy { field ->
                            // Custom mapping: Replace camelCase with JSON-style keys
                            when (field.name) {
                                "exerciseName" -> "Exercise Name"
                                "mainMuscle" -> "Main_muscle"
                                "mechanics" -> "Mechanics"
                                "suitability" -> "Suitability"
                                else -> field.name // Fallback to default name
                            }
                        }
                        .create()

                    val jsonPayload = gson.toJson(payload) // Convert map to JSON string
                    Log.d("TrainingScreen", "JSON payload: $jsonPayload")
                    val requestBody = jsonPayload.toRequestBody("application/json; charset=utf-8".toMediaType())
                    val request = Request.Builder()
                        .url("http://192.168.101.2:8080/recommend") // Use your PC's IP address
                        .post(requestBody)
                        .build()

                    Log.d("TrainingScreen", "Request created, sending API call...")
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.e("TrainingScreen", "API request failed: ${e.message}")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val responseBody = response.body?.string()
                            Log.d("TrainingScreen", "API response received: $responseBody")

                            if (response.isSuccessful && responseBody != null) {
                                Log.d("TrainingScreen", "API response is successful.")
                                // Parse the response
                                val parsedRecommendations: List<Recommendation> = gson.fromJson(
                                    responseBody,
                                    object : TypeToken<List<Recommendation>>() {}.type
                                )

                                // Update the recommendations state
                                recommendations.value = parsedRecommendations
                                Log.d("TrainingScreen", "Recommendations updated: $parsedRecommendations")
                            } else {
                                Log.e("TrainingScreen", "API error: ${response.code} - ${response.message}")
                            }
                        }
                    })
                }
            }
        }



        Spacer(modifier = Modifier.height(32.dp))

        // Recommendations List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Make it take up remaining space
                .padding(8.dp)
        ) {
            items(recommendations.value) { recommendation ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Gray, // Border color
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp) // Padding inside the border
                ) {
                    Text(
                        text = recommendation.exerciseName ?: "Unknown Exercise",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Targeted Muscle: ${recommendation.mainMuscle ?: "Unknown"}",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    )
                    Text(
                        text = "Suitability: ${recommendation.suitability}",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            color = Color.Gray
                        )
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp)) // Add space between the list and bottom components
    }


}




data class Recommendation(
    val exerciseName: String?,
    val mainMuscle: String?,
    val mechanics: String?,
    val suitability: Float
)









@Composable
private fun Levels(
    selectedLevel: String,
    backgroundColor: Color,
    selected: Boolean,
    onClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(Rounded16)
            .background(backgroundColor)
            .clickable(indication = null, interactionSource = null) {
                onClick(selectedLevel)
            }
            .padding(8.dp),
    ) {
        Text(
            text = selectedLevel,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}




data class User(
    val id: String,
    val fitness_level: String,
    val has_injury_back: Boolean,
    val has_injury_knee: Boolean,
    val has_injury_shoulder: Boolean,
)

fun fetchUserData2(supabaseClient: SupabaseClient, userId: String): User? = runBlocking {
    try {
        val postgrest = supabaseClient.postgrest
        val response = postgrest["users"]
            .select(columns = Columns.list("id", "fitness_level", "has_injury_back", "has_injury_knee", "has_injury_shoulders"))
            .data // Fetch raw JSON string
        Log.d("TrainingScreen", "Raw Response: $response")  // Log raw response
        // Parse the JSON response with Gson

        val gson = Gson()
        val userType = object : TypeToken<List<User>>() {}.type
        val users: List<User> = gson.fromJson(response, userType)

        // Return the user matching the ID
        users.find { it.id == userId }
    } catch (e: Exception) {
        println("Error fetching user data: ${e.message}")
        null
    }
}