package com.gradproj.optibodyai.main_activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gradproj.optibodyai.R
import com.gradproj.optibodyai.components.BottomNavBar
import com.gradproj.optibodyai.components.NavItem
import com.gradproj.optibodyai.ui.theme.OptibodyAITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OptibodyAITheme {
                val navController = rememberNavController()
                ActivityContent(navController)
            }
        }
    }
}

private val NavItems = listOf(
    NavItem(
        R.drawable.dashboard, // Icon resource
        label = "Dashboard",
        destination = "dashboard" // Match the NavGraph route for Dashboard
    ),
    NavItem(
        R.drawable.training, // Icon resource
        label = "Training",
        destination = "training" // Match the NavGraph route for Training
    ),
    NavItem(
        R.drawable.nutration, // Icon resource
        label = "Nutrition",
        destination = "nutrition" // Match the NavGraph route for Nutrition
    ),
    NavItem(
        R.drawable.profile, // Icon resource
        label = "Profile",
        destination = "profile" // Match the NavGraph route for Profile
    ),
)

@Composable
fun ActivityContent(navController: NavHostController) {
    Log.d("MainActivity", "Loading MainActivity Content")

    OptibodyAITheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Log.d("MainActivity", "Setting up NavGraph")
                    NavGraph(navController)
                }
                BottomNavBar(NavItems, navController) { item ->
                    navController.navigate(item.destination) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun GreetingPreview() {
    val navController = rememberNavController()
    ActivityContent(navController)
}
