package com.example.jonathan.testinfotainment.cluster.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathan.testinfotainment.cluster.domain.ClusterEntity
import com.example.jonathan.testinfotainment.cluster.domain.usecase.ClusterGetStateUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Cluster section using MVI Clean Architecture.
 * Receives intents and updates UI state with system settings and GNSS location properties.
 *
 * @property clusterGetStateUseCase Use case for observing cluster data updates.
 */
class ClusterViewModel(
    private val clusterGetStateUseCase: ClusterGetStateUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ClusterState())
    val state: StateFlow<ClusterState> = _state.asStateFlow()

    private val intentChannel = Channel<ClusterIntent>(Channel.UNLIMITED)

    init {
        // Sequentially process incoming intents from the channel
        viewModelScope.launch {
            intentChannel.receiveAsFlow().collect { intent ->
                handleIntent(intent)
            }
        }

        // Collect cluster data from domain use case
        viewModelScope.launch {
            clusterGetStateUseCase().collect { entity ->
                onIntent(ClusterIntent.PlatformRefreshStateIntent(entity))
            }
        }
    }

    /**
     * Public function to dispatch intents to the ViewModel.
     *
     * @param intent The cluster intent to process.
     */
    fun onIntent(intent: ClusterIntent) {
        intentChannel.trySend(intent)
    }

    /**
     * Handles incoming MVI intents and updates the UI state accordingly.
     */
    private fun handleIntent(intent: ClusterIntent) {
        when (intent) {
            is ClusterIntent.PlatformRefreshStateIntent -> {
                updateStateFromEntity(intent.entity)
            }
        }
    }

    private fun updateStateFromEntity(entity: ClusterEntity) {
        _state.update {
            it.copy(
                brightness = entity.brightness,
                latitude = entity.latitude,
                longitude = entity.longitude,
            )
        }
    }
}
