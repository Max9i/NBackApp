package com.emmajson.nbackapp.ui.screencomponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emmajson.nbackapp.ui.theme.NBack_CImplTheme
import com.emmajson.nbackapp.ui.viewmodels.FakeVM
import com.emmajson.nbackapp.ui.viewmodels.GameType
import com.emmajson.nbackapp.ui.viewmodels.GameViewModel

@Composable
fun GameModeToggleBar(vm: GameViewModel) {
    NBack_CImplTheme {
        // State to hold the current game mode
        val gameState by vm.gameState.collectAsState()
        val selectedGameMode = gameState.gameType

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Game Mode",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Row of buttons for each game mode
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GameType.values().forEach { mode ->
                    GameModeButton(
                        gameMode = mode,
                        isSelected = selectedGameMode == mode,
                        onClick = { vm.setGameType(mode) }
                    )
                }
            }

            // Display current selected mode
            Text(
                text = "Current Mode: ${selectedGameMode.name}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun GameModeButton(gameMode: GameType, isSelected: Boolean, onClick: () -> Unit) {
    NBack_CImplTheme {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                contentColor = if (isSelected) Color.White else Color.Black
            ),
            modifier = Modifier
                .padding(horizontal = 4.dp)
        ) {
            Text(text = gameMode.name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameModeToggleBar() {
    GameModeToggleBar(FakeVM())
}

