package com.gradproj.optibodyai.starter_activity.screens

import  androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api

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
import com.gradproj.optibodyai.components.EllipseHorizontal
import com.gradproj.optibodyai.components.ScreenHeader
import com.gradproj.optibodyai.components.TextFieldComponent




@Composable
fun EmailPasswordScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    Box {
        EllipseHorizontal()
        Column {
            ScreenHeader(
                navController,
                "Please Enter Your Email and Password",
            )

            Text(
                modifier = Modifier.padding(horizontal = 46.dp),
                text = "In order to create your account, we need your email and a secure password.",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
            )

            Column(Modifier.padding(horizontal = 18.dp)) {
                // Email Field
                TextFieldComponent(
                    headerName = "Email",
                    placeholder = "Enter your email",
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = if (email.isNotEmpty() && !email.contains("@")) {
                            "Invalid email format"
                        } else {
                            ""
                        }
                    }
                )

                // Email Validation Message
                if (emailError.isNotEmpty()) {
                    Text(
                        text = emailError,
                        color = androidx.compose.ui.graphics.Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                }

                // Password Field
                TextFieldComponent(
                    headerName = "Password",
                    placeholder = "Enter your password",
                    value = password,
                    onValueChange = { password = it },
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Pushes content up

            // Save and continue Button
            Button(
                onClick = {
                    if (emailError.isEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                        navController.navigate("profile_data_filling") // Replace with actual destination
                    }
                },
                modifier = Modifier
                    .align(CenterHorizontally)
                    .fillMaxWidth(0.75f)
            ) {
                Text(
                    text = "Save and continue",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldComponent(
    headerName: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None
) {
    Column {
        Text(
            text = headerName,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = androidx.compose.ui.graphics.Color.LightGray, // Placeholder color
                    fontSize = 14.sp
                )
            },
            visualTransformation = visualTransformation,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent, // Transparent background
                unfocusedBorderColor = androidx.compose.ui.graphics.Color.LightGray, // Light gray border
                focusedBorderColor = androidx.compose.ui.graphics.Color.White, // White border when focused
                cursorColor = androidx.compose.ui.graphics.Color.White // Cursor color
            ),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = androidx.compose.ui.graphics.Color.White // Input text color
            )
        )
    }
}




