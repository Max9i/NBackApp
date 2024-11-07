package com.emmajson.nbackapp.ui.screencomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.emmajson.nbackapp.ui.theme.NBack_CImplTheme
import com.emmajson.nbackapp.ui.viewmodels.GameViewModel

@Composable
fun GridView(vm: GameViewModel, highlightedIndex: Int) {
    val gameState by vm.gameState.collectAsState()
    val gridSize = gameState.gameGridSize
    val gridContainerSize = 300.dp  // Set the overall grid size here
    val boxSize = gridContainerSize / gridSize  // Calculate box size based on grid dimension

    NBack_CImplTheme {
        // Centering the grid within a fixed container size
        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            modifier = Modifier.size(gridContainerSize),
            content = {
                items(gridSize * gridSize) { index ->
                    val isVisible = index == highlightedIndex

                    // Main Box for the grid item with calculated size
                    Box(
                        modifier = Modifier
                            .size(boxSize)  // Use the calculated box size here
                            .padding(5.dp)
                            .clip(RoundedCornerShape(percent = 16))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                    ) {
                        if (isVisible) {
                            AnimatedVisibilityBox(isVisible = isVisible, vm)
                        }
                    }
                }
            }
        )
    }
}

