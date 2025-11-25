package com.studyhelpers.app.ui.focustimer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusTimerScreen(
    onBack: () -> Unit,
    viewModel: FocusTimerViewModel = hiltViewModel()
) {

    var uiState by remember { mutableStateOf(viewModel.state) }

    LaunchedEffect(viewModel.state) {
        uiState = viewModel.state
    }

    val minutes = uiState.seconds / 60
    val seconds = uiState.seconds % 60
    val timeText = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Focus Timer") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = timeText,
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (uiState.isRunning) {
                        viewModel.pause()
                    } else {
                        viewModel.start()
                    }
                    uiState = viewModel.state
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isRunning) "Pause" else "Start")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.reset()
                    uiState = viewModel.state
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset")
            }
        }
    }
}