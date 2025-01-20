package com.gradproj.optibodyai.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ScreenHeader(navController: NavHostController, title: String) {
    Row {
        BackButton(navController)
        Column(Modifier.weight(1f)) {
            Spacer(modifier = Modifier.height(34.25.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
        }
        Spacer(modifier = Modifier.width(43.dp))
    }
}

@Composable
fun ScreenHeader(title: String, fontSize: TextUnit = 24.sp) {
    Row {
        Column(Modifier.weight(1f)) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.width(43.dp))
    }
}