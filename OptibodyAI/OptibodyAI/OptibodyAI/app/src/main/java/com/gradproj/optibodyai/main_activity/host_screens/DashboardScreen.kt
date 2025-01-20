package com.gradproj.optibodyai.main_activity.host_screens

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.gradproj.optibodyai.R
import com.gradproj.optibodyai.chat_gpt_activity.ChatGPTActivity
import com.gradproj.optibodyai.components.ScreenHeader

import com.gradproj.optibodyai.ui.theme.Rounded16

val LetsGoBackgroundColor = Color(0xFF252727)
val ActivityBoarderColor = Color(0xFFd0d0d4)
val BarsColor = Color(0xFFfffbfe)

val WeekDays = mapOf(
    "Mon" to "Monday",
    "Tue" to "Tuesday",
    "Wed" to "Wednesday",
    "Thu" to "Thursday",
    "Fri" to "Friday",
    "Sat" to "Saturday",
    "Sun" to "Sunday"
)


@Composable
fun DashboardScreen(navController: NavHostController) {
    Column(Modifier.padding(horizontal = 18.dp)) {
        ScreenHeader(title = "Hello, Mohammad")
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "My Plan",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Cards(navController)

        ScreenHeader(title = "Weekly Stats")

        Surface(
            shape = Rounded16,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxSize(),
            color = ActivityBoarderColor
        ) {
            Surface(
                shape = Rounded16,
                modifier = Modifier
                    .padding(1.dp)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                var mostActive by remember { mutableStateOf("Fri") }

                Column {
                    Text(
                        text = "Most Active: $mostActive",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = BarsColor
                    )

                    Columns(mostActive) {
                        mostActive = it
                    }
                }
            }
        }
    }
}

@Composable
fun Columns(mostActive: String, onClick: (String) -> Unit) {
    Row(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        for (day in WeekDays) {
            Column(
                Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onClick(day.key)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val backgroundColor by animateColorAsState(
                    targetValue = if (mostActive == day.key) MaterialTheme.colorScheme.primary else BarsColor,
                    label = ""
                )

                Surface(
                    color = backgroundColor,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp)
                        .weight(1f),
                    content = {})
                HorizontalDivider(
                    color = Color.White,
                    thickness = 1.dp,
                    modifier = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(
                                constraints.copy(
                                    maxWidth = constraints.maxWidth + 100.dp.roundToPx(), //add the end padding 16.dp
                                )
                            )
                            layout(placeable.width, placeable.height) {
                                placeable.place(8.dp.roundToPx(), 0)
                            }
                        }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = day.key,
                    color = BarsColor,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun Cards(navController: NavHostController) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.height(IntrinsicSize.Max)
    ) {

        Card(
            Modifier.weight(3f)
        ) {
            CardText(
                "Workout",
                "2 Hours",
                painterResource(id = R.drawable.workout)
            ) {
            }
        }

        val context = LocalContext.current

        Card(
            Modifier
                .weight(2f)
        ) {
            CardText(
                "ChatGPT",
                "Bot",
                painterResource(id = R.drawable.robot)
            ) {
                context.startActivity(Intent(context, ChatGPTActivity::class.java))
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row {
        Card(
            Modifier
                .weight(3f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LetsGoBackgroundColor)
                    .clickable {
                        navController.navigate("training") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .width(IntrinsicSize.Max),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Let’s Go",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Surface(
                        shape = Rounded16,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(12.dp),
                        color = MaterialTheme.colorScheme.primary,
                        content = {}
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(2f))

        Card(
            Modifier
                .weight(3f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LetsGoBackgroundColor)
                    .clickable {
                        navController.navigate("food") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .width(IntrinsicSize.Max),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Food",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Surface(
                        shape = Rounded16,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(12.dp),
                        color = MaterialTheme.colorScheme.primary,
                        content = {}
                    )
                }
            }
        }

    }
}

@Composable
private fun CardText(title: String, subtitle: String, painter: Painter, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Icon(painter = painter, contentDescription = null, tint = Color.Unspecified)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Text(text = subtitle)
        }
    }
}
