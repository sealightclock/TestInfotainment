package com.example.jonathan.testinfotainment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jonathan.testinfotainment.common.AppContainer
import com.example.jonathan.testinfotainment.hvac.presentation.HvacViewModel

/**
 * Factory for creating ViewModels with their required dependencies.
 * Uses the [AppContainer] to retrieve the necessary use cases or repositories.
 *
 * @property appContainer The container providing application-scoped dependencies.
 */
class ViewModelFactory(private val appContainer: AppContainer) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the requested [ViewModel] class.
     * Special handling: Maps [HvacViewModel] to its corresponding constructor with dependencies.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HvacViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                HvacViewModel(appContainer.hvacUseCase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
