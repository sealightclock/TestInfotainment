package com.example.jonathan.testinfotainment.hvac.presentation

import com.example.jonathan.testinfotainment.hvac.domain.HvacEntity

sealed class HvacIntent {
    object TogglePower : HvacIntent()
    object IncreaseTemperature : HvacIntent()
    object DecreaseTemperature : HvacIntent()
    object IncreaseFanSpeed : HvacIntent()
    object DecreaseFanSpeed : HvacIntent()
    object ToggleFrontDefroster : HvacIntent()
    data class RefreshFromPlatform(val entity: HvacEntity) : HvacIntent()
}
