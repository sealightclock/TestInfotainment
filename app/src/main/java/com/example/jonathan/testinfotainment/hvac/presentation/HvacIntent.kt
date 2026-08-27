package com.example.jonathan.testinfotainment.hvac.presentation

/**
 * List of supported Intents (or events)
 */
sealed class HvacIntent {
    // User Intents for Power button:
    object TogglePower : HvacIntent()

    // User Intents for Temperature
    object IncreaseTemperature : HvacIntent()
    object DecreaseTemperature : HvacIntent()

    // User Intents for Fan Speed:
    object IncreaseFanSpeed : HvacIntent()
    object DecreaseFanSpeed : HvacIntent()

    // User Intents for Front Defroster:
    object ToggleFrontDefroster : HvacIntent()

    // Platform Intents:
    data class RefreshPower(val isPowerOn: Boolean) : HvacIntent()
    data class RefreshTemperature(val temperature: Int) : HvacIntent()
    data class RefreshFanSpeed(val fanSpeed: Int) : HvacIntent()
    data class RefreshFrontDefroster(val isFrontDefrosterOn: Boolean) : HvacIntent()

    // Timer Intents are implemented as delays. Refer to "Constants" for more details.
}
