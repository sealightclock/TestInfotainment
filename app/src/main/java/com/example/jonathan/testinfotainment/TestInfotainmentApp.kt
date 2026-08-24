package com.example.jonathan.testinfotainment

import android.app.Application
import com.example.jonathan.testinfotainment.common.AppContainer
import com.example.jonathan.testinfotainment.common.AppContainerImpl

/**
 * Custom [Application] class for the TestInfotainment app.
 */
class TestInfotainmentApp : Application() {
    // Instance of AppContainer that will be used by all the Activities of the app
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}

