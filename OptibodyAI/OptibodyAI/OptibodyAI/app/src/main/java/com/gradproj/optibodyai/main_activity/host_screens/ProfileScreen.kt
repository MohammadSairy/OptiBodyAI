package com.gradproj.optibodyai.main_activity.host_screens

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gradproj.optibodyai.ui.theme.Secondary
import com.google.gson.Gson
import kotlinx.coroutines.withContext
import kotlinx.coroutines.runBlocking
import com.google.gson.reflect.TypeToken
import io.github.jan.supabase.SupabaseClient // For initializing Supabase client
import io.github.jan.supabase.postgrest.query.PostgrestQueryBuilder // To use `eq` and other filters
import kotlinx.coroutines.runBlocking // For coroutine scope
import kotlinx.serialization.decodeFromString // JSON decoding (if needed)
import kotlinx.coroutines.runBlocking
import io.github.jan.supabase.postgrest.postgrest



@Composable
fun ProfileScreen(navController: NavHostController) {
    // State to hold user data
    val name = remember { mutableStateOf("") }
    val age = remember { mutableStateOf("") }
    val height = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(true) }

    // Fetch user data from Supabase
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val userId = supabase.auth.currentSessionOrNull()?.user?.id
                Log.d("ProfileScreen", "Fetching data for user ID: $userId")
                val userData = fetchUserData(
                    supabaseClient = supabase,
                    userId = userId ?: throw Exception("User ID not found")
                )
                withContext(Dispatchers.Main) {
                    Log.d("ProfileScreen", "Fetched user data: $userData") // Log fetched data
                    name.value = userData?.name ?: "Unknown"
                    Log.d("ProfileScreen", "Name updated: ${name.value}") // Log the updated name
                    age.value = userData?.age?.toString() ?: "Unknown"
                    Log.d("ProfileScreen", "Age updated: ${age.value}") // Log the updated age
                    height.value = userData?.height?.toString() ?: "Unknown"
                    Log.d("ProfileScreen", "Height updated: ${height.value}") // Log the updated height
                    location.value = userData?.location ?: "Unknown"
                    Log.d("ProfileScreen", "Location updated: ${location.value}") // Log the updated location
                    isLoading.value = false
                }
            } catch (e: Exception) {
                Log.e("ProfileScreen", "Error fetching user data: ${e.message}")
                withContext(Dispatchers.Main) {
                    name.value = "Error loading data"
                    age.value = "Error loading data"
                    height.value = "Error loading data"
                    location.value = "Error loading data"
                    isLoading.value = false
                }
            }
        }
    }

    Column(Modifier.padding(horizontal = 18.dp)) {
        ScreenHeader("Profile Data")

        Text(
            modifier = Modifier.padding(horizontal = 46.dp),
            text = "Your profile data",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
        )

        if (isLoading.value) {
            Text(
                text = "Loading...",
                modifier = Modifier.align(CenterHorizontally),
                fontSize = 16.sp
            )
        } else {
            Column(Modifier.padding(horizontal = 18.dp)) {
                ScreenHeader("Profile Data")

                Text(
                    modifier = Modifier.padding(horizontal = 46.dp),
                    text = "Your profile data",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                )

                // Display fields using the new `ProfileReadOnlyField`
                ProfileReadOnlyField(label = "Name", value = name.value)
                ProfileReadOnlyField(label = "Age", value = age.value)
                ProfileReadOnlyField(label = "Height", value = height.value)
                ProfileReadOnlyField(label = "Location", value = location.value)

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(32.dp))
            }

        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(32.dp))
    }
}

fun fetchUserData(supabaseClient: SupabaseClient, userId: String): User? = runBlocking {
    try {
        val postgrest = supabaseClient.postgrest
        // Get the raw JSON string
        val response = postgrest["users"]
            .select(columns = Columns.list("id", "name", "age", "height", "location"))
            .data // Get raw data as a JSON string

        println("Raw JSON response: $response")

        // Parse the response using Gson
        val gson = Gson()
        val userType = object : TypeToken<List<User>>() {}.type
        val users: List<User> = gson.fromJson(response, userType)

        // Find the user with the matching ID
        val user = users.find { it.id == userId }
        println("User data retrieved: $user")
        user
    } catch (e: Exception) {
        println("Error fetching user data: ${e.message}")
        null
    }
}






data class User(
    val id: String,
    val name: String,
    val age: Int,
    val height: Double,
    val location: String
)

@Composable
fun ProfileReadOnlyField(
    label: String,
    value: String
) {
    Log.d("ProfileReadOnlyField", "Rendering $label with value: $value") // Log received values
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
             // Ensure background contrast
    ) {
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.Black, // Explicit color for visibility
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )

        Surface(
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 2.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

