package com.gradproj.optibodyai.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun LoadingCircle(size: Dp = 120.dp, navigate: () -> Unit = {}) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        var isExpanded by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            while (true) {
                isExpanded = !isExpanded
                delay(1000)
            }
        }

        LaunchedEffect(Unit) {
            delay(4000)
            navigate()
        }

        val circleSize by animateDpAsState(
            targetValue = if (isExpanded) size else 55.dp,
            label = "",
            animationSpec = tween(durationMillis = 1000)
        )

        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Color(0xff597d93)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(circleSize)
                    .background(MaterialTheme.colorScheme.secondary),
            )
        }
    }
}


