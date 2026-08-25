package com.example.jonathan.testinfotainment.hvac.presentation

import com.example.jonathan.testinfotainment.common.Constants
import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity

/**
 * Represents the UI state for the HVAC (Heating, Ventilation, and Air Conditioning) system.
 *
 * @property hvac The core HVAC data entity containing current settings.
 * @property isPowerButtonEnabled Whether the power button is currently interactive.
 * @property isFrontDefrosterButtonEnabled Whether the front defroster button is currently interactive.
 */
data class HvacState(
    val hvac: HvacEntity = HvacEntity(),
    val isPowerButtonEnabled: Boolean = true,
    val isFrontDefrosterButtonEnabled: Boolean = true
) {
    /**
     * Helper to check if the HVAC power is on.
     */
    val isPowerOn get() = hvac.isPowerOn

    /**
     * Helper to check if the front defroster is on.
     */
    val isFrontDefrosterOn get() = hvac.isFrontDefrosterOn

    /**
     * The effective temperature to display.
     * Special handling: When front defroster is active, temperature is maxed out.
     */
    val temperature: Int
        get() = if (hvac.isFrontDefrosterOn) Constants.TEMPERATURE_MAX else hvac.temperature

    /**
     * The effective fan speed to display.
     * Special handling: When front defroster is active, fan speed is maxed out.
     */
    val fanSpeed: Int
        get() = if (hvac.isFrontDefrosterOn) Constants.FAN_SPEED_MAX else hvac.fanSpeed
}
