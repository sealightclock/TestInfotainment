package com.example.jonathan.testinfotainment.cluster.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * View composable for the Cluster section in the vehicle display.
 * Displays system settings (system brightness) and location properties (GNSS coordinates).
 *
 * @param viewModel The [ClusterViewModel] providing cluster state.
 */
@Composable
fun ClusterScreen(viewModel: ClusterViewModel) {
    val state by viewModel.state.collectAsState()
    ClusterScreenContent(state = state)
}

/**
 * Stateless content composable for rendering Cluster properties.
 *
 * @param state Current [ClusterState] containing system and location settings.
 */
@Composable
fun ClusterScreenContent(state: ClusterState) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "EV Cluster Dashboard",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // System Setting Property: Brightness
                ClusterPropertyCard(
                    icon = Icons.Default.Brightness6,
                    label = "System Brightness",
                    value = "${state.brightness}%",
                )

                // Location Property: GNSS Coordinates
                ClusterPropertyCard(
                    icon = Icons.Default.LocationOn,
                    label = "GNSS Location",
                    value = state.formattedCoordinates,
                )
            }
        }
    }
}

/**
 * Reusable card composable for displaying a read-only cluster property item.
 *
 * @param icon Representative icon.
 * @param label Title label for the property.
 * @param value Formatted string value of the property.
 */
@Composable
fun ClusterPropertyCard(
    icon: ImageVector,
    label: String,
    value: String,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 600, heightDp = 150)
@Composable
fun ClusterScreenContentPreview() {
    ClusterScreenContent(state = ClusterState())
}
