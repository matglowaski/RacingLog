package com.example.racinglog.ui.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.racinglog.data.model.Lap
import com.example.racinglog.data.model.LapTimeUtils
import com.example.racinglog.data.repository.RacingCatalogRepository
import com.example.racinglog.viewmodel.HistoryViewModel
import androidx.compose.ui.Alignment

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel
) {

    var lapToDelete by remember {
        mutableStateOf<Lap?>(null)
    }

    LaunchedEffect(Unit) {
        viewModel.loadLaps()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        Column {
            Text(
                "RACING LOG / REGISTROS",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Histórico de voltas",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Todas as suas sessões registradas.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        OutlinedButton(
            onClick = { viewModel.loadLaps() },
            enabled = !viewModel.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ATUALIZAR HISTÓRICO")
        }

        if (viewModel.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        viewModel.errorMessage?.let { error ->
            Text(
                error,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (
            !viewModel.isLoading &&
            viewModel.laps.isEmpty() &&
            viewModel.errorMessage == null
        ) {
            Text(
                "Nenhuma volta registrada ainda.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(
                items = viewModel.laps,
                key = { it.id }
            ) { lap ->
                LapHistoryCard(
                    lap = lap,
                    onDeleteClick = {
                        lapToDelete = lap
                    }
                )
            }
        }
    }
    lapToDelete?.let { lap ->
        AlertDialog(
            onDismissRequest = {
                lapToDelete = null
            },
            title = {
                Text("Excluir volta?")
            },
            text = {
                Text(
                    "Deseja realmente excluir esta volta? " +
                            "Essa ação não pode ser desfeita."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteLap(lap.id)
                        lapToDelete = null
                    }
                ) {
                    Text(
                        "EXCLUIR",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        lapToDelete = null
                    }
                ) {
                    Text("CANCELAR")
                }
            }
        )
    }
}


@Composable
private fun LapHistoryCard(
    lap: Lap,
    onDeleteClick: () -> Unit
) {
    val catalog = remember { RacingCatalogRepository() }

    val trackName = remember(lap.simulatorId, lap.trackId) {
        catalog.getTracks(lap.simulatorId)
            .find { it.id == lap.trackId }
            ?.name ?: lap.trackId
    }

    val carName = remember(lap.simulatorId, lap.carId) {
        catalog.getCars(lap.simulatorId)
            .find { it.id == lap.carId }
            ?.name ?: lap.carId
    }

    val simulatorName = when (lap.simulatorId) {
        "lmu" -> "LE MANS ULTIMATE"
        "ams2" -> "AUTOMOBILISTA 2"
        "iracing" -> "IRACING"
        "acc" -> "ASSETTO CORSA COMPETIZIONE"
        else -> lap.simulatorId.uppercase()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {



            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = simulatorName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onDeleteClick) {
                    Text(
                        text = "🗑",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }


            Text(
                text = trackName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )


            Text(
                carName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                LapTimeUtils.formatTime(lap.totalTimeMs),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            if (
                lap.sector1Ms != null &&
                lap.sector2Ms != null &&
                lap.sector3Ms != null
            ) {
                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SectorHistoryCard(
                        "S1",
                        lap.sector1Ms,
                        Modifier.weight(1f)
                    )

                    SectorHistoryCard(
                        "S2",
                        lap.sector2Ms,
                        Modifier.weight(1f)
                    )

                    SectorHistoryCard(
                        "S3",
                        lap.sector3Ms,
                        Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectorHistoryCard(
    label: String,
    timeMs: Long,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                LapTimeUtils.formatTime(timeMs),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
