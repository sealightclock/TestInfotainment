package com.example.jonathan.testinfotainment.hvac.presentation

/**
 * List of supported Intents (or events)
 *     [1] Use "object" if there is no argument list, for efficiency.
 *     [2] Use "data class" if there is argument list.
 */
sealed class HvacIntent {
    // User Intents
    // ==========

    // User Intents for Power button:
    object UserTogglePowerIntent : HvacIntent()

    // User Intents for Temperature
    object UserIncreaseTemperatureIntent : HvacIntent()
    object UserDecreaseTemperatureIntent : HvacIntent()

    // User Intents for Fan Speed:
    object UserIncreaseFanSpeedIntent : HvacIntent()
    object UserDecreaseFanSpeedIntent : HvacIntent()

    // User Intents for Front Defroster:
    object UserToggleFrontDefrosterIntent : HvacIntent()

    // Platform Intents
    // ==========

    data class PlatformRefreshPowerIntent(val isPowerOn: Boolean) : HvacIntent()
    data class PlatformRefreshTemperatureIntent(val temperature: Int) : HvacIntent()
    data class PlatformRefreshFanSpeedIntent(val fanSpeed: Int) : HvacIntent()
    data class PlatformRefreshFrontDefrosterIntent(val isFrontDefrosterOn: Boolean) : HvacIntent()

    // Timer Intents are implemented as delays. Refer to file "Constants.kt" for more details.
}
