package com.emmajson.nbackapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.emmajson.nbackapp.data.UserPreferencesRepository
import com.emmajson.nbackapp.navigation.Screen
import com.emmajson.nbackapp.ui.screencomponents.GameModeToggleBar
import com.emmajson.nbackapp.ui.screencomponents.GridView
import com.emmajson.nbackapp.ui.screencomponents.NumberInputSlider
import com.emmajson.nbackapp.ui.theme.NBack_CImplTheme
import com.emmajson.nbackapp.ui.viewmodels.FakeVM
import com.emmajson.nbackapp.ui.viewmodels.GameState
import com.emmajson.nbackapp.ui.viewmodels.GameVM
import com.emmajson.nbackapp.ui.viewmodels.GameViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(vm: GameViewModel, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()

    NBack_CImplTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Button(
                    modifier = Modifier
                        .height(75.dp)
                        .weight(0.2f),
                    shape = MaterialTheme.shapes.medium,
                    onClick = {
                        vm.stopGame()
                        navController.navigate(Screen.HomeScreen.route)
                    }
                ) {
                    Text(
                        text = "Back",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }

                Spacer(Modifier.width(100.dp))

                Button(
                    modifier = Modifier
                        .height(75.dp)
                        .weight(0.2f),
                    shape = MaterialTheme.shapes.medium,
                    // Trigger save action on ViewModel
                    onClick = {
                        coroutineScope.launch {
                            vm.saveCurrentSettings()
                        }
                    }  // Save only when Save button is pressed
                ) {
                    Text(
                        text = "Save",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White

                    )
                }
            }
        }

    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp) // Top padding to offset content
            .background(MaterialTheme.colorScheme.background)
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .verticalScroll(scrollState) // Enable vertical scrolling for the entire column
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp), // Optional: Padding around content for spacing
            verticalArrangement = Arrangement.spacedBy(16.dp) // Space between items
        ) {
            // Game Mode Toggle Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                GameModeToggleBar(vm)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(16.dp) // Optional padding within the Box
            ) {
                NumberInputSlider(vm, 9, "Game Length")
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(16.dp) // Optional padding within the Box
            ) {
                NumberInputSlider(vm, 10, "NBack Level")
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(16.dp) // Optional padding within the Box
            ) {
                NumberInputSlider(vm, 2, "Grid Size")
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(16.dp) // Optional padding within the Box
            ) {
                NumberInputSlider(vm, 4, "Round Duration")
            }
        }
    }
}


@Preview
@Composable
fun SettingsScreenPreview() {
    NBack_CImplTheme {
        Surface {
            SettingsScreen(FakeVM(), navController = rememberNavController())
        }
    }
}
