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

private const val TAG = "TIF: MainScreen"

enum class Screen(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Default.Home),
    HVAC("HVAC", Icons.Default.Thermostat),
    Settings("Settings", Icons.Default.Settings)
}

@Composable
fun MainScreen() {
    Log.i(TAG, "MainScreen")

    var currentScreen by remember { mutableStateOf(Screen.Home) }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // [2] Initially, there are 3 buttons: "Home", "HVAC", "Settings".
            Screen.entries.forEach { screen ->
                NavigationRailItem(
                    selected = currentScreen == screen, // [3] Highlighted
                    onClick = { currentScreen = screen }, // [3] Clicking each button will display the corresponding screen.
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
            ScreenContent(screen = currentScreen)
        }
    }
}

@Composable
fun ScreenContent(screen: Screen) {
    Text(text = "${screen.label} Screen")
}

@Preview(showBackground = true, widthDp = 800, heightDp = 480)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
