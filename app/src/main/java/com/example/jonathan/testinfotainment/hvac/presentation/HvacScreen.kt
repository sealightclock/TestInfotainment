package com.example.jonathan.testinfotainment.hvac.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jonathan.testinfotainment.hvac.common.Constants

/**
 * The main UI screen for HVAC controls.
 * Displays power, temperature, fan speed, and defroster controls.
 *
 * @param viewModel The ViewModel providing the HVAC state and handling user intents.
 */
@Composable
fun HvacScreen(viewModel: HvacViewModel) {
    val state by viewModel.state.collectAsState()
    HvacScreenContent(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

/**
 * Stateless content composable for HVAC screen, enabling previews and flexible layout.
 *
 * @param state The current state of the HVAC system.
 * @param onIntent Callback to send user intents.
 */
@Composable
fun HvacScreenContent(
    state: HvacState,
    onIntent: (HvacIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Row 1: Mode toggle buttons (Power and Front Defroster)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HvacControlButton(
                icon = Icons.Default.PowerSettingsNew,
                isSelected = state.isPowerOn,
                onClick = { onIntent(HvacIntent.UserTogglePowerIntent) },
                label = if (state.isPowerOn) "ON" else "OFF",
                isPrimaryAction = true,
                enabled = state.isPowerButtonEnabled,
            )

            HvacControlButton(
                icon = Icons.Default.WindPower,
                isSelected = state.isFrontDefrosterOn,
                enabled = state.isPowerOn && state.isFrontDefrosterButtonEnabled,
                onClick = { onIntent(HvacIntent.UserToggleFrontDefrosterIntent) },
                label = "FRONT",
            )
        }

        // Row 2: Value adjustment controls (Temperature and Fan Speed)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HvacValueControl(
                icon = Icons.Default.DeviceThermostat,
                value = when (state.temperature) {
                    Constants.TEMPERATURE_MIN -> "LO"
                    Constants.TEMPERATURE_MAX -> "HI"
                    else -> "${state.temperature}°F"
                },
                enabled = state.isPowerOn && !state.isFrontDefrosterOn,
                onIncrease = { onIntent(HvacIntent.UserIncreaseTemperatureIntent) },
                onDecrease = { onIntent(HvacIntent.UserDecreaseTemperatureIntent) },
            )

            HvacValueControl(
                icon = Icons.Default.Air,
                value = state.fanSpeed.toString(),
                enabled = state.isPowerOn && !state.isFrontDefrosterOn,
                onIncrease = { onIntent(HvacIntent.UserIncreaseFanSpeedIntent) },
                onDecrease = { onIntent(HvacIntent.UserDecreaseFanSpeedIntent) },
            )
        }
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
    isPrimaryAction: Boolean = false,
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
            },
        ),
        modifier = Modifier.size(width = 100.dp, height = 52.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(20.dp))
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
    onDecrease: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrease, enabled = enabled, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease")
            }
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (enabled) MaterialTheme.colorScheme.onSurface else Color.Gray,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
            IconButton(onClick = onIncrease, enabled = enabled, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 150)
@Composable
fun HvacScreenContentPreview() {
    HvacScreenContent(
        state = HvacState(),
        onIntent = {},
    )
}
