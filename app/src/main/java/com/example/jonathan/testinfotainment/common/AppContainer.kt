package com.example.jonathan.testinfotainment.common

import android.content.Context
import com.example.jonathan.testinfotainment.hvac.data.HvacLocalDataSource
import com.example.jonathan.testinfotainment.hvac.data.HvacPlatformDataSource
import com.example.jonathan.testinfotainment.hvac.data.HvacRepositoryImpl
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository
import com.example.jonathan.testinfotainment.hvac.domain.usecase.*

/**
 * Dependency Container interface for the application.
 * Defines the public dependencies available throughout the app.
 */
interface AppContainer {
    /**
     * Use cases for HVAC operations used by ViewModels.
     */
    val hvacGetStateUseCase: HvacGetStateUseCase
    val hvacUserTogglePowerUseCase: HvacUserTogglePowerUseCase
    val hvacUserAdjustTemperatureUseCase: HvacUserAdjustTemperatureUseCase
    val hvacUserAdjustFanSpeedUseCase: HvacUserAdjustFanSpeedUseCase
    val hvacUserToggleFrontDefrosterUseCase: HvacUserToggleFrontDefrosterUseCase
}

/**
 * Concrete implementation of the [AppContainer].
 * Lazily initializes and provides application-scoped dependencies.
 *
 * @param context The application context.
 */
class AppContainerImpl(private val context: Context) : AppContainer {

    /**
     * Local data source for HVAC settings (e.g., DataStore).
     */
    private val hvacLocalDataSource: HvacLocalDataSource by lazy {
        HvacLocalDataSource(context.applicationContext)
    }

    /**
     * Platform data source simulating vehicle hardware (VHAL).
     */
    private val hvacPlatformDataSource: HvacPlatformDataSource by lazy {
        HvacPlatformDataSource()
    }

    /**
     * Repository coordinating local and platform HVAC data.
     */
    private val hvacRepository: HvacRepository by lazy {
        HvacRepositoryImpl(hvacLocalDataSource, hvacPlatformDataSource)
    }

    // --- Public Use Cases ---

    override val hvacGetStateUseCase: HvacGetStateUseCase by lazy {
        HvacGetStateUseCase(hvacRepository)
    }

    override val hvacUserTogglePowerUseCase: HvacUserTogglePowerUseCase by lazy {
        HvacUserTogglePowerUseCase(hvacRepository, storeIsPowerOnToLocalUseCase)
    }

    override val hvacUserAdjustTemperatureUseCase: HvacUserAdjustTemperatureUseCase by lazy {
        HvacUserAdjustTemperatureUseCase(hvacRepository, storeTemperatureToLocalUseCase)
    }

    override val hvacUserAdjustFanSpeedUseCase: HvacUserAdjustFanSpeedUseCase by lazy {
        HvacUserAdjustFanSpeedUseCase(
            hvacRepository,
            storeFanSpeedToLocalUseCase,
            storeIsPowerOnToLocalUseCase,
        )
    }

    override val hvacUserToggleFrontDefrosterUseCase: HvacUserToggleFrontDefrosterUseCase by lazy {
        HvacUserToggleFrontDefrosterUseCase(hvacRepository, storeIsFrontDefrosterOnToLocalUseCase)
    }

    // --- Internal Persistence Use Cases (Private) ---

    private val storeIsPowerOnToLocalUseCase: HvacStoreIsPowerOnToLocalUseCase by lazy {
        HvacStoreIsPowerOnToLocalUseCase(hvacRepository)
    }

    private val storeTemperatureToLocalUseCase: HvacStoreTemperatureToLocalUseCase by lazy {
        HvacStoreTemperatureToLocalUseCase(hvacRepository)
    }

    private val storeFanSpeedToLocalUseCase: HvacStoreFanSpeedToLocalUseCase by lazy {
        HvacStoreFanSpeedToLocalUseCase(hvacRepository)
    }

    private val storeIsFrontDefrosterOnToLocalUseCase: HvacStoreIsFrontDefrosterOnToLocalUseCase by lazy {
        HvacStoreIsFrontDefrosterOnToLocalUseCase(hvacRepository)
    }
}
