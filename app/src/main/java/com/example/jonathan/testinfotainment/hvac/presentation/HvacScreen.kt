package com.example.jonathan.testinfotainment.hvac.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jonathan.testinfotainment.common.Constants

/**
 * The main UI screen for HVAC controls.
 * Displays power, temperature, fan speed, and defroster controls.
 *
 * @param viewModel The ViewModel providing the HVAC state and handling user intents.
 */
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
        // Power Button: Toggles the entire HVAC system on or off.
        HvacControlButton(
            icon = Icons.Default.PowerSettingsNew,
            isSelected = state.isPowerOn,
            onClick = { viewModel.onIntent(HvacIntent.UserTogglePowerIntent) },
            label = if (state.isPowerOn) "ON" else "OFF",
            isPrimaryAction = true,
            enabled = state.isPowerButtonEnabled
        )

        // Temperature Control: Disabled when power is off or front defroster is active.
        HvacValueControl(
            icon = Icons.Default.DeviceThermostat,
            value = when (state.temperature) {
                Constants.TEMPERATURE_MIN -> "LO"
                Constants.TEMPERATURE_MAX -> "HI"
                else -> "${state.temperature}°F"
            },
            enabled = state.isPowerOn && !state.isFrontDefrosterOn,
            onIncrease = { viewModel.onIntent(HvacIntent.UserIncreaseTemperatureIntent) },
            onDecrease = { viewModel.onIntent(HvacIntent.UserDecreaseTemperatureIntent) }
        )

        // Fan Speed Control: Disabled when power is off or front defroster is active.
        HvacValueControl(
            icon = Icons.Default.Air,
            value = state.fanSpeed.toString(),
            enabled = state.isPowerOn && !state.isFrontDefrosterOn,
            onIncrease = { viewModel.onIntent(HvacIntent.UserIncreaseFanSpeedIntent) },
            onDecrease = { viewModel.onIntent(HvacIntent.UserDecreaseFanSpeedIntent) }
        )

        // Front Defroster: Toggles the defroster mode. Forces max temp and fan speed when active.
        HvacControlButton(
            icon = Icons.Default.WindPower,
            isSelected = state.isFrontDefrosterOn,
            enabled = state.isPowerOn && state.isFrontDefrosterButtonEnabled,
            onClick = { viewModel.onIntent(HvacIntent.UserToggleFrontDefrosterIntent) },
            label = "FRONT"
        )
    }
}

/**
 * A reusable button for HVAC mode controls (Power, Defroster).
 *
 * @param icon The icon to display.
 * @param isSelected True if the mode is currently active.
 * @param onClick Callback for button click.
 * @param label The text label to display below the icon.
 * @param enabled True if the button should be interactive.
 * @param isPrimaryAction True if this button represents a major action like Power.
 */
@Composable
fun HvacControlButton(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    label: String,
    enabled: Boolean = true,
    isPrimaryAction: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else if (isPrimaryAction) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
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

/**
 * A reusable control for adjusting numeric or textual values with increase/decrease buttons.
 *
 * @param icon The representative icon for the control.
 * @param value The current value to display.
 * @param enabled True if the control should be interactive.
 * @param onIncrease Callback for increasing the value.
 * @param onDecrease Callback for decreasing the value.
 */
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
