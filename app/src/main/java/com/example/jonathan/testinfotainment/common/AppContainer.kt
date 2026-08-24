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
interface AppContainer {
    val hvacUseCase: HvacUseCase
}

class AppContainerImpl(private val context: Context) : AppContainer {

    private val hvacLocalDataSource: HvacLocalDataSource by lazy {
        HvacLocalDataSource(context.applicationContext)
    }

    private val hvacPlatformDataSource: HvacPlatformDataSource by lazy {
        HvacPlatformDataSource()
    }

    private val hvacRepository: HvacRepository by lazy {
        HvacRepositoryImpl(hvacLocalDataSource, hvacPlatformDataSource)
    }

    override val hvacUseCase: HvacUseCase by lazy {
        HvacUseCase(hvacRepository)
    }
}
