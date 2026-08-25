package com.example.jonathan.testinfotainment.common

import android.content.Context
import com.example.jonathan.testinfotainment.hvac.data.HvacLocalDataSource
import com.example.jonathan.testinfotainment.hvac.data.HvacPlatformDataSource
import com.example.jonathan.testinfotainment.hvac.data.HvacRepositoryImpl
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository
import com.example.jonathan.testinfotainment.hvac.domain.HvacUseCase

/**
 * Dependency Container for the application.
 * This is the "Composition Root" where all dependencies are wired together.
 */
/**
 * Dependency Container interface for the application.
 * Defines the dependencies available throughout the app.
 */
interface AppContainer {
    /**
     * Provides the singleton instance of the HVAC UseCase.
     */
    val hvacUseCase: HvacUseCase
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

    override val hvacUseCase: HvacUseCase by lazy {
        HvacUseCase(hvacRepository)
    }
}
