package com.gradproj.optibodyai.starter_activity.screens

import  androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem

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
fun ProfileDataFillingScreen(navController: NavHostController) {
    var selectedGoal by remember { mutableStateOf("Select Goal") } // State for the selected dropdown option
    val goals = listOf("Lose Weight", "Maintain Weight", "Gain Weight") // Dropdown options

    Box {
        EllipseHorizontal()
        Column {
            ScreenHeader(
                navController,
                "Please Enter Your Profile Data",
            )

            Text(
                modifier = Modifier.padding(horizontal = 46.dp),
                text = "In order to make full use of this app, we need the most accurate info you can provide.",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
            )

            Column(Modifier.padding(horizontal = 18.dp)) {
                TextFieldComponent(headerName = "Name", placeholder = "Enter Name")
                TextFieldComponent(headerName = "Age", placeholder = "Enter Age")
                TextFieldComponent(headerName = "Height", placeholder = "Enter Height")
                TextFieldComponent(headerName = "Weight", placeholder = "Enter Weight")
                TextFieldComponent(headerName = "Location", placeholder = "Enter Location")

                Spacer(modifier = Modifier.height(16.dp))


            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    // Handle navigation and calorie calculation based on selectedGoal
                    navController.navigate("profile_data_filling_continue")
                },
                modifier = Modifier
                    .align(CenterHorizontally)
                    .fillMaxWidth(0.75f)
            ) {
                Text(text = "Save and continue", fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun DropdownMenuComponent(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = selectedOption,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { expanded = true },
            fontSize = 14.sp
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                ) {
                    Text(text = option)
                }
            }
        }
    }
}





