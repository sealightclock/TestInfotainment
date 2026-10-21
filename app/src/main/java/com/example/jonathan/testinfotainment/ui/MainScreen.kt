package com.example.jonathan.testinfotainment.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jonathan.testinfotainment.hvac.presentation.HvacScreen
import com.example.jonathan.testinfotainment.hvac.presentation.HvacViewModel
import com.example.jonathan.testinfotainment.hvac.presentation.HvacViewModelFactory

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
 * The root Composable for the application's UI, breaking the full screen into 3 vertical sections:
 * 1. Upper Section (30% height): "Infotainment Section" containing the original navigation rail and content.
 * 2. Middle Section (40% height): "Cluster Section" reserved for EV safety-critical driving data.
 * 3. Bottom Section (30% height): "Navigation Section" reserved for maps and 3rd-party navigation SDK.
 */
@Composable
fun MainScreen() {
    Log.i(TAG, "MainScreen")

    val hvacViewModel: HvacViewModel = viewModel(
        factory = HvacViewModelFactory(LocalContext.current.applicationContext),
    )

    var currentScreen by remember { mutableStateOf(Screen.Home) }

    Column(modifier = Modifier.fillMaxSize()) {
        // [1] Upper Section: 30% of total height - Infotainment Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f),
        ) {
            InfotainmentSection(
                currentScreen = currentScreen,
                onScreenSelected = { currentScreen = it },
                hvacViewModel = hvacViewModel,
            )
        }

        HorizontalDivider()

        // [2] Middle Section: 40% of total height - Cluster Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
        ) {
            ClusterSection()
        }

        HorizontalDivider()

        // [3] Bottom Section: 30% of total height - Navigation Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f),
        ) {
            NavigationSection()
        }
    }
}

/**
 * Displays the Infotainment section occupying the upper 30% of the screen height.
 * Contains the side navigation rail and the main screen content area.
 *
 * @param currentScreen The active top-level screen.
 * @param onScreenSelected Callback when a navigation rail item is selected.
 * @param hvacViewModel The shared ViewModel for HVAC functionality.
 */
@Composable
fun InfotainmentSection(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    hvacViewModel: HvacViewModel,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Navigation side rail
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Screen.entries.forEach { screen ->
                NavigationRailItem(
                    selected = currentScreen == screen,
                    onClick = { onScreenSelected(screen) },
                    icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                    label = { Text(screen.label) },
                    alwaysShowLabel = true,
                    modifier = Modifier.padding(vertical = 2.dp),
                )
            }
        }

        // Main content area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            ScreenContent(screen = currentScreen, hvacViewModel = hvacViewModel)
        }
    }
}

/**
 * Displays the Cluster section occupying the middle 40% of the screen height.
 * Reserved for displaying safety-critical data of an EV car.
 */
@Composable
fun ClusterSection() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Cluster Section",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reserved for safety-critical EV car data",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/**
 * Displays the Navigation section occupying the bottom 30% of the screen height.
 * Reserved for handling maps and navigation using a 3rd-party navigation SDK.
 */
@Composable
fun NavigationSection() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Navigation Section",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reserved for 3rd-party maps & navigation SDK",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
    }
}

/**
 * Renders the content of the currently selected screen in the Infotainment section.
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
    // In a real app, you would provide a mock factory or mock dependencies here for the preview.
    // For now, this is a placeholder.
}
