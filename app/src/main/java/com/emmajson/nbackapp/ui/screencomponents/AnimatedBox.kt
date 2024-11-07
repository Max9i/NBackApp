package com.emmajson.nbackapp.ui.screencomponents

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.emmajson.nbackapp.ui.theme.NBack_CImplTheme
import com.emmajson.nbackapp.ui.viewmodels.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun AnimatedVisibilityBox(isVisible: Boolean, vm: GameViewModel) {
    NBack_CImplTheme {
        val gameState by vm.gameState.collectAsState()
        val rondDuration = gameState.rondDuration
        val visibleDurationMillis = rondDuration.toInt()/2
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
            initialValue = MaterialTheme.colorScheme.primaryContainer,
            targetValue = MaterialTheme.colorScheme.onPrimaryContainer,
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
                    .background(color)
            )
        }
    }
}
