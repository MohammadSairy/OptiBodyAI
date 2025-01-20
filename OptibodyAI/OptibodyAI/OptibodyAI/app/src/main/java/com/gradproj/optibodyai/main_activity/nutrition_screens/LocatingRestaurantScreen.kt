package com.gradproj.optibodyai.main_activity.nutrition_screens

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
import com.gradproj.optibodyai.components.LoadingCircle
import com.gradproj.optibodyai.components.ScreenHeader
import com.gradproj.optibodyai.main_activity.host_screens.SELECT_RESTAURANT_SCREEN
import com.gradproj.optibodyai.ui.theme.SubTitle


@Composable
fun LocatingRestaurantScreen(navController: NavHostController) {
    Column(Modifier.padding(horizontal = 18.dp)) {
        ScreenHeader(title = "Locating a restaurant near you...")
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "This might take a while...",
            fontWeight = FontWeight.Bold,
            color = SubTitle
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingCircle {
                navController.navigate(SELECT_RESTAURANT_SCREEN)
            }
        }
    }
}