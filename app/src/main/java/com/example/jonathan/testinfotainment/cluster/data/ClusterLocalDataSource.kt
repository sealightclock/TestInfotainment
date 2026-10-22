package com.example.jonathan.testinfotainment.cluster.data

import com.example.jonathan.testinfotainment.cluster.domain.ClusterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Local data source for caching or providing default cluster properties.
 */
class ClusterLocalDataSource : ClusterDataSource {
    private val _clusterState = MutableStateFlow(ClusterEntity())

    override val clusterState: Flow<ClusterEntity> = _clusterState.asStateFlow()
}
