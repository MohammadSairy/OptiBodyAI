package com.gradproj.optibodyai.main_activity

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gradproj.optibodyai.main_activity.host_screens.DashboardScreen
import com.gradproj.optibodyai.main_activity.host_screens.NutritionScreen
import com.gradproj.optibodyai.main_activity.host_screens.ProfileScreen
import com.gradproj.optibodyai.main_activity.host_screens.TrainingScreen
import com.gradproj.optibodyai.main_activity.host_screens.FoodScreen
import kotlinx.serialization.Serializable


@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "dashboard",
    ) {
        Log.d("NavGraph", "Starting navigation with DashboardDestination")

        composable("dashboard") {
            Log.d("NavGraph", "Navigating to Dashboard")
            DashboardScreen(navController)
        }
        composable("training") {
            Log.d("NavGraph", "Composing Training Screen")
            TrainingScreen(navController)
        }

        composable("nutrition") {
            Log.d("NavGraph", "Navigating to Nutrition")

            NutritionScreen(navController)
        }
        composable("food") {
            // Replace this with your actual FoodScreen composable
            Log.d("NavGraph", "Navigating to Food")
            FoodScreen(navController)
        }

        composable("profile") {
            Log.d("NavGraph", "Navigating to Profile")

            ProfileScreen(navController)
        }
    }
}

