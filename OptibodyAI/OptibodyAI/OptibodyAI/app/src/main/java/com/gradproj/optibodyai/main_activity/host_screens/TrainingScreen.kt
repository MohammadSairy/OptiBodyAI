package com.gradproj.optibodyai.main_activity.host_screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gradproj.optibodyai.main_activity.training_screens.LocatingGymScreen
import com.gradproj.optibodyai.main_activity.training_screens.SelectGymScreen
import com.gradproj.optibodyai.main_activity.training_screens.SelectProgramScreen
import kotlinx.serialization.Serializable

const val LOCATING_GYM_SCREEN = "LocatingGymScreen"
const val SELECT_GYM_SCREEN = "SelectGymScreen"
const val SELECT_PROGRAM_SCREEN = "SelectProgramScreen"

@Composable
fun TrainingScreen(mainScreensNavController: NavHostController) {
    android.util.Log.d("TrainingScreen", "Initializing TrainingScreen")

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LOCATING_GYM_SCREEN) {
        composable(LOCATING_GYM_SCREEN) {
            android.util.Log.d("TrainingScreen", "Navigating to LocatingGymScreen")
            LocatingGymScreen(navController)
        }

        composable(SELECT_GYM_SCREEN) {
            android.util.Log.d("TrainingScreen", "Navigating to SelectGymScreen")
            SelectGymScreen(navController)
        }

        composable(SELECT_PROGRAM_SCREEN) {
            android.util.Log.d("TrainingScreen", "Navigating to SelectProgramScreen")
            SelectProgramScreen(navController)
        }
    }
}

