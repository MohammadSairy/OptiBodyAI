package com.gradproj.optibodyai.starter_activity

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gradproj.optibodyai.starter_activity.screens.ProfileDataFillingContinueScreen
import com.gradproj.optibodyai.starter_activity.screens.ProfileDataFillingScreen
import com.gradproj.optibodyai.starter_activity.screens.StarterScreen
import com.gradproj.optibodyai.starter_activity.screens.EmailPasswordScreen
import com.gradproj.optibodyai.starter_activity.screens.LoginScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "starter",
    ) {
        composable("starter") {
            StarterScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("email_password") {
            EmailPasswordScreen(navController)
        }
        composable("profile_data_filling") {
            ProfileDataFillingScreen(navController)
        }
        composable("profile_data_filling_continue") {
            ProfileDataFillingContinueScreen(navController)
        }
    }
}
