package com.emmajson.nbackapp.ui.screens

import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.emmajson.nbackapp.navigation.Screen
import com.emmajson.nbackapp.ui.screencomponents.GridView
import com.emmajson.nbackapp.ui.theme.NBack_CImplTheme
import com.emmajson.nbackapp.ui.viewmodels.FakeVM
import com.emmajson.nbackapp.ui.viewmodels.GameViewModel

@Composable
fun HomeScreen(vm: GameViewModel, navController: NavController) {
    NBack_CImplTheme {
        val gameState by vm.gameState.collectAsState()
        val lengthValue = gameState.gameLength
        val nBackValue = gameState.gameNBackLvl
        val rondDuration = (gameState.rondDuration.toFloat()/1000)
        val selectedGameMode = gameState.gameType

        val highscore by vm.highscore.collectAsState()
        val snackBarHostState = remember { SnackbarHostState() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Top, // Space items vertically
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "High-Score = $highscore",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Box (modifier = Modifier.padding(40.dp)) {
                Text(
                    text = "Number of Sequences :    $lengthValue" + '\n' +
                            "Time per Sequence :    $rondDuration s" + '\n' +
                            "n = $nBackValue",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.onSurface)
                        .fillMaxWidth()
                        .padding(0.dp,10.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(20.dp,0.dp,20.dp,100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GridView(vm, 5)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(20.dp,0.dp,20.dp,100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
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
                        text = "Play".uppercase(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Box {
                Button(
                    onClick = { navController.navigate(Screen.SettingsScreen.route) },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .fillMaxWidth(0.4f) // Adjust width
                        .height(75.dp)      // Standard button height
                        .padding(vertical = 16.dp)
                        .clip(RoundedCornerShape(100))
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = "Settings".uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(20.dp,100.dp,20.dp,20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box {
                Text(
                    text = "Gametype: $selectedGameMode".uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
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
