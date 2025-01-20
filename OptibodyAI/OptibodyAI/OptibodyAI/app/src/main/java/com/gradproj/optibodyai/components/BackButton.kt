package com.gradproj.optibodyai.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

val BackButtonBoarderColor = Color(0xFFe3e5e5)

@Composable
fun BackButton(navController: NavHostController) {
    Box(
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp, end = 3.dp)
            .size(35.dp)
            .clip(CircleShape)
            .background(BackButtonBoarderColor)
            .clickable { navController.navigateUp() },
    ) {
        Box(
            modifier = Modifier
                .padding(1.dp)
                .clip(CircleShape)
                .background(Color.White)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }
}
