package com.example.jonathan.testinfotainment.cluster.data

import com.example.jonathan.testinfotainment.cluster.domain.ClusterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Data source simulating device system settings and GNSS location platform sensors.
 */
class ClusterPlatformDataSource : ClusterDataSource {
    private val _clusterState = MutableStateFlow(
        ClusterEntity(
            brightness = 85,
            latitude = 37.7749,
            longitude = -122.4194,
        ),
    )

    override val clusterState: Flow<ClusterEntity> = _clusterState.asStateFlow()
}
