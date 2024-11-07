package com.emmajson.nbackapp.ui.screencomponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emmajson.nbackapp.ui.theme.NBack_CImplTheme
import com.emmajson.nbackapp.ui.viewmodels.FakeVM
import com.emmajson.nbackapp.ui.viewmodels.GameViewModel

@Composable
fun NumberInputSlider(vm: GameViewModel, steps: Int, text: String ) {
    NBack_CImplTheme {
        val gameState by vm.gameState.collectAsState()
        val sliderValue = gameState.gameLength

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

            Text(
                text = sliderValue.toString(),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Slider(
                value = sliderValue.toFloat(),
                onValueChange = { newValue -> vm.setGameLength(newValue.toInt()) },
                valueRange = 0f..100f,
                steps = steps,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNumberInputSlider() {
    NumberInputSlider(FakeVM(),9,"Length")
}
