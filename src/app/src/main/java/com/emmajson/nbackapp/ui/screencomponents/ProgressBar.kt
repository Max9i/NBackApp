package com.emmajson.nbackapp.ui.screencomponents

import android.content.res.Resources.Theme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import com.emmajson.nbackapp.ui.theme.NBack_CImplTheme

@Composable
fun ProgressBar(progressValue: Int, maximum: Int) {
    NBack_CImplTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Custom Progress Bar using Box for thickness and full width
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)  // Adjust the thickness here
                    .background(MaterialTheme.colorScheme.primaryContainer)  // Background for the progress bar
            ) {
                // Filled part of the progress bar
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(
                            fraction = (progressValue.toFloat() / maximum.toFloat()).coerceIn(
                                0f,
                                1f
                            )
                        )  // Width based on progress
                        .background(MaterialTheme.colorScheme.onPrimaryContainer)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomProgressBar() {
    ProgressBar(progressValue = 50, maximum = 100)  // Set progressValue to a test value like 50 for testing
}
