package com.gradproj.optibodyai.main_activity.host_screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gradproj.optibodyai.main_activity.nutrition_screens.LocatingRestaurantScreen
import com.gradproj.optibodyai.main_activity.nutrition_screens.SelectRestaurantScreen

// Define route names as constants
const val LOCATING_RESTAURANT_SCREEN = "LocatingRestaurantScreen"
const val SELECT_RESTAURANT_SCREEN = "SelectRestaurantScreen"

@Composable
fun NutritionScreen(hostNavController: NavHostController) {
    val navController = rememberNavController()

    Log.d("NutritionScreen", "Initializing NutritionScreen")

    NavHost(navController = navController, startDestination = LOCATING_RESTAURANT_SCREEN) {
        composable(LOCATING_RESTAURANT_SCREEN) {
            Log.d("NutritionScreen", "Navigating to LocatingRestaurantScreen")
            LocatingRestaurantScreen(navController)
        }

        composable(SELECT_RESTAURANT_SCREEN) {
            Log.d("NutritionScreen", "Navigating to SelectRestaurantScreen")
            SelectRestaurantScreen(navController)
        }
    }
}
