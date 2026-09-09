package com.example.jonathan.testinfotainment.hvac.presentation

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity

/**
 * List of supported Intents (or events) for the HVAC system.
 * Handles both user interactions and platform state updates.
 */
sealed class HvacIntent {
    // User Intents
    // ==========

    /** User toggled the main power button. */
    object UserTogglePowerIntent : HvacIntent()

    /** User wants to increase the target temperature. */
    object UserIncreaseTemperatureIntent : HvacIntent()
    /** User wants to decrease the target temperature. */
    object UserDecreaseTemperatureIntent : HvacIntent()

    /** User wants to increase the fan speed. */
    object UserIncreaseFanSpeedIntent : HvacIntent()
    /** User wants to decrease the fan speed. */
    object UserDecreaseFanSpeedIntent : HvacIntent()

    /** User toggled the front defroster. */
    object UserToggleFrontDefrosterIntent : HvacIntent()

    // Platform Intents
    // ==========

    /**
     * Intent to refresh the entire HVAC state from the platform.
     * Consolidates individual property updates to reduce intent queue overhead.
     *
     * @property entity The new HVAC state from the platform.
     */
    data class PlatformRefreshStateIntent(val entity: HvacEntity) : HvacIntent()
}
