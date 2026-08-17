package com.example.jonathan.testinfotainment.hvac.presentation

sealed class HvacIntent {
    object TogglePower : HvacIntent()
    object IncreaseTemperature : HvacIntent()
    object DecreaseTemperature : HvacIntent()
    object IncreaseFanSpeed : HvacIntent()
    object DecreaseFanSpeed : HvacIntent()
    object ToggleFrontDefroster : HvacIntent()
}
