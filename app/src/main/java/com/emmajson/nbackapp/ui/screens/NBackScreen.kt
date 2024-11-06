package com.emmajson.nbackapp.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.emmajson.nbackapp.R
import com.emmajson.nbackapp.ui.viewmodels.FakeVM
import com.emmajson.nbackapp.ui.viewmodels.GameViewModel

@Composable
fun NBackScreen(vm: GameViewModel, navController: NavController) {
    val highscore by vm.highscore.collectAsState()  // Highscore is its own StateFlow
    val gameState by vm.gameState.collectAsState()
    val currentscore by vm.score.collectAsState()
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

            Row {
                Button(
                    shape = RectangleShape,
                    onClick = vm::startGame
                ) {
                    val displayText = if (gameState.eventValue == -1) {
                        "Start Game"
                    } else {
                        "Score: $currentscore"
                    }
                    Text(
                        text = displayText,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Wrapping the LazyGrid in another view (Box in this case)
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .weight(1f) // This allows the grid to expand and use available space
            ) {
                GridView(highlightedIndex = highlightedIndex)
            }

            // Footer row with two buttons
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
                        vm.checkMatchAudio(vm.currentIndex.value)
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Hey! you clicked the audio button"
                            )
                        }
                    }
                ) {
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
                        vm.checkMatchPlacement(vm.currentIndex.value)
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Hey! you clicked the vision button"
                            )
                        }
                    }
                ) {
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

@Preview(showBackground = true)
@Composable
fun NBackScreenPreview() {
    // Mock ViewModel or pass a fake ViewModel for preview purposes
    val fakeVm = FakeVM() // You can create a FakeVM class or just pass a mock instance
    NBackScreen(vm = fakeVm, rememberNavController())
}