package com.example.jonathan.testinfotainment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jonathan.testinfotainment.common.AppContainer
import com.example.jonathan.testinfotainment.hvac.presentation.HvacViewModel

/**
 * Factory for creating ViewModels using the AppContainer.
 */
class ViewModelFactory(private val appContainer: AppContainer) : ViewModelProvider.Factory {

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
