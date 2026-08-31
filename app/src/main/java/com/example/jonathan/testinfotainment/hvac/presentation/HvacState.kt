package com.example.jonathan.testinfotainment.hvac.presentation

import com.example.jonathan.testinfotainment.common.Constants

/**
 * Represents the UI state for the HVAC (Heating, Ventilation, and Air Conditioning) system.
 *
 * @property isPowerOn True if the HVAC system is powered on.
 * @property temperature The effective temperature to display.
 * @property fanSpeed The effective fan speed to display.
 * @property isFrontDefrosterOn True if the front defroster is active.
 * @property isPowerButtonEnabled Whether the power button is currently interactive.
 * @property isFrontDefrosterButtonEnabled Whether the front defroster button is currently interactive.
 */
data class HvacState(
    val isPowerOn: Boolean = true, // HVAC system power state
    val temperature: Int = Constants.TEMPERATURE_DEFAULT, // Effective temperature to display
    val fanSpeed: Int = Constants.FAN_SPEED_MIN, // Effective fan speed to display
    val isFrontDefrosterOn: Boolean = false, // Front defroster state
    val isPowerButtonEnabled: Boolean = true, // HVAC Power button accessibility state
    val isFrontDefrosterButtonEnabled: Boolean = true // HVAC Front Defroster accessibility state
)
