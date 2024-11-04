package com.emmajson.nbackapp.ui.screens

import android.graphics.drawable.shapes.Shape
import android.widget.GridLayout
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.emmajson.nbackapp.R
import com.emmajson.nbackapp.ui.viewmodels.FakeVM
import com.emmajson.nbackapp.ui.viewmodels.GameVM
import com.emmajson.nbackapp.ui.viewmodels.GameViewModel

@Composable
fun NBackScreen(
    vm: GameViewModel
) {
    val highscore by vm.highscore.collectAsState()  // Highscore is its own StateFlow
    val gameState by vm.gameState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val highlightedIndex = gameState.eventValue - 1 // Get the index to highlight

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(32.dp),
                text = "High-Score = $highscore",
                style = MaterialTheme.typography.headlineLarge
            )

            Row{
                Button(
                    shape = RectangleShape,
                    onClick = vm::startGame) {
                    val displayText = if (gameState.eventValue == -1) {
                    "Start Game"
                    } else {
                    "Current eventValue is: ${gameState.eventValue}"
                        // TODO: CHange this button later : Might want other values here...
                }
                    Text(
                        text = displayText,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Box(
                modifier =  Modifier.padding(20.dp),
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3)
                ) {
                    items(3 * 3) { index ->
                        // Determine if this box should be highlighted6

                        val isHighlighted = index == highlightedIndex
                        val animatedColor by animateColorAsState(
                            targetValue = if (isHighlighted) Color(0xFFFFD0E0) else Color(0xFFB0C4DE),
                            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                        )
                        //TODO: START HERE

                        // Apply animated color if highlighted; otherwise, use default color
                        val backgroundColor = if (isHighlighted) animatedColor else Color(0xFFB0C4DE)

                        // Define shape and border
                        val borderColor = Color.White
                        val shape = RoundedCornerShape(16)

                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(shape)
                                .border(5.dp, borderColor, shape)
                                .background(backgroundColor),
                            contentAlignment = Alignment.Center
                        ) {
                            // You could add additional content here if needed
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),

                    shape = RectangleShape,
                    onClick = {
                    // Todo: change this button behaviour
                    scope.launch {
                        snackBarHostState.showSnackbar(
                            message = "Hey! you clicked the audio button"
                        )
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.sound_on),
                        contentDescription = "Sound",
                        modifier = Modifier
                            .height(48.dp)
                            .aspectRatio(3f / 2f)
                    )
                }

                Spacer(Modifier.width(4.dp))

                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RectangleShape,
                    onClick = {
                        // Todo: change this button behaviour
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Hey! you clicked the visual button",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.visual),
                        contentDescription = "Visual",
                        modifier = Modifier
                            .height(48.dp)
                            .aspectRatio(3f / 2f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun NBackScreenPreview() {
    // Since I am injecting a VM into my homescreen that depends on Application context, the preview doesn't work.
    Surface(){
        NBackScreen(FakeVM())
    }
}