package com.emmajson.nbackapp.ui.screencomponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emmajson.nbackapp.ui.theme.NBack_CImplTheme
import com.emmajson.nbackapp.ui.viewmodels.FakeVM
import com.emmajson.nbackapp.ui.viewmodels.GameViewModel

@Composable
fun NumberInputSlider(vm: GameViewModel, steps: Int, text: String ) {
    NBack_CImplTheme {
        val gameState by vm.gameState.collectAsState()
        val lengthValue = gameState.gameLength
        val nBackValue = gameState.gameNBackLvl
        val gridSize = gameState.gameGridSize
        val rondDuration = gameState.rondDuration

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (text == "Game Length") {
                Text(
                    text = lengthValue.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Slider(
                    value = lengthValue.toFloat(),
                    onValueChange = { newLenValue -> vm.setGameLength(newLenValue.toInt()) },
                    valueRange = 0f..100f,
                    steps = steps,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            else if (text == "NBack Level") {
                Text(
                    text = nBackValue.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Slider(
                    value = nBackValue.toFloat(),
                    onValueChange = { newNBackVal -> vm.setNBack(newNBackVal.toInt()) },
                    valueRange = 1f..12f,
                    steps = steps,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            else if (text == "Grid Size") {
                Text(
                    text = gridSize.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Slider(
                    value = gridSize.toFloat(),
                    onValueChange = { newGridVal -> vm.setGridSize(newGridVal.toInt()) },
                    valueRange = 2f..5f,
                    steps = steps,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            else if (text == "Round Duration") {
                Text(
                    text = rondDuration.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Slider(
                    value = rondDuration.toFloat(),
                    onValueChange = { newRondVal -> vm.setDuration(newRondVal.toLong()) },
                    valueRange = 500f..3000f,
                    steps = steps,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNumberInputSlider() {
    NumberInputSlider(FakeVM(),9,"Length")
}
