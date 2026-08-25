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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jonathan.testinfotainment.TestInfotainmentApp
import com.example.jonathan.testinfotainment.common.AppContainer
import com.example.jonathan.testinfotainment.hvac.presentation.HvacScreen
import com.example.jonathan.testinfotainment.hvac.presentation.HvacViewModel

private const val TAG = "TIF: MainScreen"

/**
 * Defines the available top-level screens in the application.
 *
 * @property label The display name of the screen.
 * @property icon The icon associated with the screen.
 */
enum class Screen(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Default.Home),
    HVAC("HVAC", Icons.Default.Thermostat),
    Settings("Settings", Icons.Default.Settings)
}

/**
 * The root Composable for the application's UI, featuring a side navigation bar and a content area.
 *
 * @param appContainer The dependency injection container for providing view models.
 */
@Composable
fun MainScreen(
    appContainer: AppContainer = (androidx.compose.ui.platform.LocalContext.current.applicationContext as TestInfotainmentApp).container
) {
    Log.i(TAG, "MainScreen")

    val hvacViewModel: HvacViewModel = viewModel(
        factory = ViewModelFactory(appContainer)
    )

    var currentScreen by remember { mutableStateOf(Screen.Home) }

    Row(modifier = Modifier.fillMaxSize()) {
        // Navigation side rail
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

/**
 * Renders the content of the currently selected screen.
 *
 * @param screen The active screen to display.
 * @param hvacViewModel The shared HVAC view model.
 */
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
    // In a real app, you would provide a MockAppContainer here for the preview
    // For now, this might still fail if the context isn't right, but it's better structured.
    // Text("Main Screen Preview")
}
