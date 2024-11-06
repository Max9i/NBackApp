package com.emmajson.nbackapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.emmajson.nbackapp.ui.theme.NBack_CImplTheme
import kotlinx.coroutines.delay

@Composable
fun GridView(highlightedIndex: Int) {
    NBack_CImplTheme {
        // The LazyVerticalGrid that displays each box
        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            items(3 * 3) { index ->
                var isVisible = index == highlightedIndex

                // Main Box for the grid item
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(percent = 16))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                ) {
                    if (isVisible) AnimatedVisibilityBox(isVisible = isVisible)
                }
            }
        }
    }
}
