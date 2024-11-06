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
import com.emmajson.nbackapp.ui.theme.NBack_CImplTheme
import com.emmajson.nbackapp.ui.viewmodels.FakeVM
import com.emmajson.nbackapp.ui.viewmodels.GameViewModel

@Composable
fun HomeScreen(vm: GameViewModel, navController: NavController) {
    NBack_CImplTheme {
        val highscore by vm.highscore.collectAsState()
        val snackBarHostState = remember { SnackbarHostState() }

        Scaffold(
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Top, // Space items vertically
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // High-Score Display at the top
                Text(
                    text = "High-Score = $highscore",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                // Start Game Button in the center


                // Spacer at the bottom to maintain spacing
                Spacer(modifier = Modifier.height(32.dp))
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding) ,
                verticalArrangement = Arrangement.Center, // Space items vertically
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate(Screen.NBackScreen.route) },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Adjust width
                        .height(120.dp)      // Standard button height
                        .padding(vertical = 16.dp)
                        .clip(RoundedCornerShape(100))
                ) {
                    Text(
                        text = "Start Game".uppercase(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = { navController.navigate(Screen.NBackScreen.route) },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth(0.6f) // Adjust width
                        .height(75.dp)      // Standard button height
                        .padding(vertical = 16.dp)
                        .clip(RoundedCornerShape(100))
                ) {
                    Text(
                        text = "Settings".uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    NBack_CImplTheme {
        Surface {
            HomeScreen(FakeVM(), navController = rememberNavController())
        }
    }
}
