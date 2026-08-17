package com.example.jonathan.testinfotainment.hvac.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HvacScreen(viewModel: HvacViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Power Button
        HvacControlButton(
            icon = Icons.Default.PowerSettingsNew,
            isSelected = state.isPowerOn,
            onClick = { viewModel.onIntent(HvacIntent.TogglePower) },
            label = if (state.isPowerOn) "ON" else "OFF"
        )

        // Temperature Control
        HvacValueControl(
            icon = Icons.Default.DeviceThermostat,
            value = "${state.temperature}°F",
            enabled = state.isPowerOn,
            onIncrease = { viewModel.onIntent(HvacIntent.IncreaseTemperature) },
            onDecrease = { viewModel.onIntent(HvacIntent.DecreaseTemperature) }
        )

        // Fan Speed Control
        HvacValueControl(
            icon = Icons.Default.Air,
            value = state.fanSpeed.toString(),
            enabled = state.isPowerOn,
            onIncrease = { viewModel.onIntent(HvacIntent.IncreaseFanSpeed) },
            onDecrease = { viewModel.onIntent(HvacIntent.DecreaseFanSpeed) }
        )

        // Front Defroster
        HvacControlButton(
            icon = Icons.Default.WindPower,
            isSelected = state.isFrontDefrosterOn,
            enabled = state.isPowerOn,
            onClick = { viewModel.onIntent(HvacIntent.ToggleFrontDefroster) },
            label = "FRONT"
        )
    }
}

@Composable
fun HvacControlButton(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    label: String,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.size(width = 120.dp, height = 80.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = label)
            Text(text = label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun HvacValueControl(
    icon: ImageVector,
    value: String,
    enabled: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrease, enabled = enabled) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease")
            }
            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = if (enabled) MaterialTheme.colorScheme.onSurface else Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            IconButton(onClick = onIncrease, enabled = enabled) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}
