package com.example.jonathan.testinfotainment.cluster.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jonathan.testinfotainment.cluster.data.ClusterLocalDataSource
import com.example.jonathan.testinfotainment.cluster.data.ClusterPlatformDataSource
import com.example.jonathan.testinfotainment.cluster.data.ClusterRepositoryImpl
import com.example.jonathan.testinfotainment.cluster.domain.usecase.ClusterGetStateUseCase

/**
 * ViewModelProvider.Factory for instantiating [ClusterViewModel] with its Clean Architecture dependencies.
 */
class ClusterViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClusterViewModel::class.java)) {
            val platformDataSource = ClusterPlatformDataSource()
            val localDataSource = ClusterLocalDataSource()
            val repository = ClusterRepositoryImpl(platformDataSource, localDataSource)
            val getStateUseCase = ClusterGetStateUseCase(repository)

            return ClusterViewModel(getStateUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
