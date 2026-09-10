package com.example.jonathan.testinfotainment.hvac.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jonathan.testinfotainment.hvac.data.HvacLocalDataSource
import com.example.jonathan.testinfotainment.hvac.data.HvacPlatformDataSource
import com.example.jonathan.testinfotainment.hvac.data.HvacRepositoryImpl
import com.example.jonathan.testinfotainment.hvac.domain.HvacRepository
import com.example.jonathan.testinfotainment.hvac.domain.usecase.*

/**
 * Factory for creating [HvacViewModel] with its required dependencies.
 * This factory maintains singletons for HVAC-related data sources and repositories
 * to ensure consistent state throughout the application.
 */
class HvacViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var instance: HvacRepository? = null

        /**
         * Provides a singleton instance of the [HvacRepository].
         */
        private fun getRepository(context: Context): HvacRepository {
            return instance ?: synchronized(this) {
                instance ?: HvacRepositoryImpl(
                    HvacLocalDataSource(context.applicationContext),
                    HvacPlatformDataSource()
                ).also { instance = it }
            }
        }
    }

    /**
     * Creates a new instance of [HvacViewModel].
     * Instantiates the necessary use cases and wires them with the repository.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HvacViewModel::class.java)) {
            val repository = getRepository(context)
            
            // --- Internal Persistence Use Cases ---
            val storeIsPowerOnToLocalUseCase = HvacStoreIsPowerOnToLocalUseCase(repository)
            val storeTemperatureToLocalUseCase = HvacStoreTemperatureToLocalUseCase(repository)
            val storeFanSpeedToLocalUseCase = HvacStoreFanSpeedToLocalUseCase(repository)
            val storeIsFrontDefrosterOnToLocalUseCase = HvacStoreIsFrontDefrosterOnToLocalUseCase(repository)

            // --- Public Use Cases for ViewModel ---
            val hvacGetStateUseCase = HvacGetStateUseCase(repository)
            val hvacUserTogglePowerUseCase = HvacUserTogglePowerUseCase(
                repository, 
                storeIsPowerOnToLocalUseCase
            )
            val hvacUserAdjustTemperatureUseCase = HvacUserAdjustTemperatureUseCase(
                repository, 
                storeTemperatureToLocalUseCase
            )
            val hvacUserAdjustFanSpeedUseCase = HvacUserAdjustFanSpeedUseCase(
                repository,
                storeFanSpeedToLocalUseCase,
                storeIsPowerOnToLocalUseCase
            )
            val hvacUserToggleFrontDefrosterUseCase = HvacUserToggleFrontDefrosterUseCase(
                repository, 
                storeIsFrontDefrosterOnToLocalUseCase
            )

            return HvacViewModel(
                hvacGetStateUseCase,
                hvacUserTogglePowerUseCase,
                hvacUserAdjustTemperatureUseCase,
                hvacUserAdjustFanSpeedUseCase,
                hvacUserToggleFrontDefrosterUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
