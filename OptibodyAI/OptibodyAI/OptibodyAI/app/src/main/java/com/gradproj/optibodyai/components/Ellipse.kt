package com.gradproj.optibodyai.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.gradproj.optibodyai.R
import kotlin.math.roundToInt

@Deprecated("old figma design")
@Composable
fun BoxScope.EllipseVertical() {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Icon(
        painter = painterResource(id = R.drawable.ellipse_644),
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxHeight()
            .width(screenWidth)
            .scale(scaleX = 4.25f, scaleY = 1.25f)
            .offset {
                IntOffset(0, (3.8 * screenWidth.value).roundToInt())
            },
        tint = Color.White
    )
}

@Composable
fun EllipseHorizontal(starterScreen: Boolean = false) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current.density
    Box(modifier = Modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = R.drawable.ellipse_644),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .scale(scaleX = 2.5f, scaleY = 1f)
                .offset {
                    IntOffset(
                        0,
                        ((if (starterScreen) 0.725f else 0.825f) * (screenHeight.value * density)).roundToInt()
                    )
                },
            tint = Color.White
        )
    }
}
