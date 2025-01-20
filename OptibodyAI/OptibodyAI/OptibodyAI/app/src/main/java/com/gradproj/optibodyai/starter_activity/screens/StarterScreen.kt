package com.gradproj.optibodyai.starter_activity.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.gradproj.optibodyai.R
import com.gradproj.optibodyai.components.EllipseHorizontal


@Composable
fun StarterScreen(navHostController: NavHostController) {
    Box {
        EllipseHorizontal(true)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(0.4f))
            Image(
                painter = painterResource(id = R.drawable.optibody),
                contentDescription = null,
                modifier = Modifier
                    .scale(scaleY = 2.75f, scaleX = 2.75f)
            )
            Spacer(modifier = Modifier.weight(0.6f))

            Button(
                onClick = { navHostController.navigate("email_password") },
                Modifier
                    .fillMaxWidth(0.75f)
            ) {
                Text(
                    text = "Get Started", fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navHostController.navigate("login") },
                Modifier
                    .fillMaxWidth(0.75f)
            ) {
                Text(
                    text = "I already have an account",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}