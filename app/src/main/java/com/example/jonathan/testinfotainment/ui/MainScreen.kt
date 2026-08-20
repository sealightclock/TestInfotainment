package com.example.jonathan.testinfotainment.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jonathan.testinfotainment.hvac.data.HvacLocalDataSource
import com.example.jonathan.testinfotainment.hvac.data.HvacPlatformDataSource
import com.example.jonathan.testinfotainment.hvac.data.HvacRepositoryImpl
import com.example.jonathan.testinfotainment.hvac.domain.HvacUseCase
import com.example.jonathan.testinfotainment.hvac.presentation.HvacScreen
import com.example.jonathan.testinfotainment.hvac.presentation.HvacViewModel

private const val TAG = "TIF: MainScreen"

enum class Screen(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Default.Home),
    HVAC("HVAC", Icons.Default.Thermostat),
    Settings("Settings", Icons.Default.Settings)
}

@Composable
fun MainScreen() {
    Log.i(TAG, "MainScreen")

    // Setup HVAC dependencies manually for this example
    // In a real app, these would be provided by a DI container like Hilt.
    val context = androidx.compose.ui.platform.LocalContext.current
    val hvacViewModel: HvacViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val localDataSource = HvacLocalDataSource(context.applicationContext)
                val platformDataSource = HvacPlatformDataSource()
                val repository = HvacRepositoryImpl(localDataSource, platformDataSource)
                val useCase = HvacUseCase(repository)
                @Suppress("UNCHECKED_CAST")
                return HvacViewModel(useCase) as T
            }
        }
    )

    var currentScreen by remember { mutableStateOf(Screen.Home) }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Screen.entries.forEach { screen ->
                NavigationRailItem(
                    selected = currentScreen == screen,
                    onClick = { currentScreen = screen },
                    icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                    label = { Text(screen.label) },
                    alwaysShowLabel = true,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        // Main content area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            ScreenContent(screen = currentScreen, hvacViewModel = hvacViewModel)
        }
    }
}

@Composable
fun ScreenContent(screen: Screen, hvacViewModel: HvacViewModel) {
    when (screen) {
        Screen.HVAC -> HvacScreen(viewModel = hvacViewModel)
        else -> Text(text = "${screen.label} Screen")
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 480)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
