package com.gradproj.optibodyai.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

data class NavItem(
    val iconRes: Int?,
    val label: String,
    val destination: String,
)

@Composable
fun BottomNavBar(
    items: List<NavItem>,
    navHostController: NavHostController,
    onItemSelected: (NavItem) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(85.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val backStackEntry by navHostController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            // Debug Logs
            Log.d("BottomNavBar", "Item destination: ${item.destination}, Current route: $currentRoute")

            val selected = currentRoute == item.destination
            NavBarItem(item, selected, onItemSelected)
        }
    }
}

@Composable
private fun RowScope.NavBarItem(
    item: NavItem,
    selected: Boolean,
    onSelect: (NavItem) -> Unit,
) {
    Column(
        Modifier
            .fillMaxHeight()
            .weight(1f)
            .clickable(indication = null, interactionSource = null) {
                Log.d("BottomNavBar", "Clicked on item with destination: ${item.destination}")
                onSelect(item)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    painter = painterResource(id = item.iconRes!!),
                    contentDescription = null,
                    tint = if (selected) MaterialTheme.colorScheme.secondary else Color.Black,
                )
                AnimatedVisibility(selected) {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
