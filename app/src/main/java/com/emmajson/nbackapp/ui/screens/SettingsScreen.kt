package com.emmajson.nbackapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.emmajson.nbackapp.navigation.Screen
import com.emmajson.nbackapp.ui.screencomponents.GameModeToggleBar
import com.emmajson.nbackapp.ui.screencomponents.GridView
import com.emmajson.nbackapp.ui.screencomponents.NumberInputSlider
import com.emmajson.nbackapp.ui.theme.NBack_CImplTheme
import com.emmajson.nbackapp.ui.viewmodels.FakeVM
import com.emmajson.nbackapp.ui.viewmodels.GameVM
import com.emmajson.nbackapp.ui.viewmodels.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(vm: GameViewModel, navController: NavController) {
    NBack_CImplTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Game Settings")
                        Button(
                            modifier = Modifier
                                .height(50.dp)
                                .width(150.dp),
                            shape = MaterialTheme.shapes.medium,
                            onClick = { navController.navigate(Screen.HomeScreen.route) }
                        ) {
                            Text(
                                text = "Back",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp), // Additional padding for content
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Add the Game Mode Toggle Bar
                Box(modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                ) {
                    GameModeToggleBar(vm)
                }

                Box(modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                ) {
                    // Add the Number Input Slider
                    NumberInputSlider(vm, 9 , "Game Length")
                }

                Box(modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                ) {
                    // Add the Number Input Slider
                    NumberInputSlider(vm, 9 , "NBack")
                }
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
