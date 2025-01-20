package com.gradproj.optibodyai.main_activity.host_screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gradproj.optibodyai.components.LoadingCircle
import com.gradproj.optibodyai.components.ScreenHeader
import com.gradproj.optibodyai.main_activity.host_screens.SELECT_RESTAURANT_SCREEN
import com.gradproj.optibodyai.main_activity.training_screens.LocatingGymScreen
import com.gradproj.optibodyai.main_activity.training_screens.SelectGymScreen
import com.gradproj.optibodyai.main_activity.training_screens.SelectProgramScreen
import com.gradproj.optibodyai.main_activity.training_screens.User
import com.gradproj.optibodyai.ui.theme.SubTitle
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.runBlocking


@Composable
fun FoodScreen(navController: NavHostController) {
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

fun fetchUserData3(supabaseClient: SupabaseClient, userId: String): User? = runBlocking {
    try {
        val postgrest = supabaseClient.postgrest
        val response = postgrest["users"]
            .select(columns = io.github.jan.supabase.postgrest.query.Columns.list("allergy_peanut", "allergy_seafood", "allergy_wheat", "allergy_Milk", "allergy_sesamy"))
            .data // Fetch raw JSON string
        Log.d("FoodScreen", "Raw Response: $response")  // Log raw response
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
data class User2(
    val allergy_peanut: Boolean,
    val allergy_seafood: Boolean,
    val allergy_wheat: Boolean,
    val allergy_milk: Boolean,
    val allergy_sesamy: Boolean,
    
)