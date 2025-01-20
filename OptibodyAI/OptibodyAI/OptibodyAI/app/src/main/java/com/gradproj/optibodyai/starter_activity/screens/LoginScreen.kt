package com.gradproj.optibodyai.starter_activity.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gradproj.optibodyai.components.TextFieldComponent
import com.gradproj.optibodyai.components.ScreenHeader
import com.gradproj.optibodyai.supabase
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import com.gradproj.optibodyai.main_activity.MainActivity
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") } // Updated variable name

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ScreenHeader(navController, "Login to Your Account")

        Column(modifier = Modifier.fillMaxWidth()) {
            // Email Field
            TextFieldComponent(
                headerName = "Email",
                placeholder = "Enter your email",
                value = email,
                onValueChange = {
                    email = it
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            TextFieldComponent(
                headerName = "Password",
                placeholder = "Enter your password",
                value = password,
                onValueChange = { password = it },
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
            )

            // Display feedback message
            if (feedbackMessage.isNotEmpty()) {
                Text(
                    text = feedbackMessage,
                    color = if (feedbackMessage == "Login successful!") androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        val context = LocalContext.current // Get the context outside of the coroutine

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Attempt to sign in the user
                        supabase.auth.signInWith(Email) {
                            this.email = email
                            this.password = password
                        }

                        // Check if the user has a valid session
                        val session = supabase.auth.currentSessionOrNull()

                        withContext(Dispatchers.Main) {
                            if (session != null) {
                                // Use the previously fetched context here
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                            } else {
                                feedbackMessage = "Invalid email or password"
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            feedbackMessage = "An error occurred: ${e.message}"
                        }
                    }
                }
            },
            modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxWidth(0.75f)
        ) {
            Text(
                text = "Login",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
