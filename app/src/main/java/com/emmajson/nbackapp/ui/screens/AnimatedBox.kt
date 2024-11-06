package com.emmajson.nbackapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AnimatedVisibilityBox(isVisible: Boolean) {
    val visibleDurationMillis: Int = 1000
    var isVisible by remember { mutableStateOf(isVisible) }

    // Trigger the visibility toggle based on the specified duration
    LaunchedEffect(Unit) {
        while (true) {
            isVisible = true
            delay(visibleDurationMillis.toLong()) // Keep visible for the specified duration
            isVisible = false
            delay(visibleDurationMillis.toLong()) // Wait before showing again
        }
    }

    // Define the color transition
    val transition = rememberInfiniteTransition()
    val color by transition.animateColor(
        initialValue = Color(0xFFB0C4DE),
        targetValue = Color(0xFF9C27B0),
        animationSpec = infiniteRepeatable(
            animation = tween(visibleDurationMillis),
            repeatMode = RepeatMode.Reverse
        )
    )

    // AnimatedVisibility with enter and exit animations having the same duration as visibleDurationMillis
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = visibleDurationMillis)),
        exit = fadeOut(animationSpec = tween(durationMillis = visibleDurationMillis))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(color)
        )
    }
}
