package com.example.racinglog.ui.screens.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.racinglog.data.model.LapTimeUtils
import com.example.racinglog.data.repository.RacingCatalogRepository
import com.example.racinglog.viewmodel.AnalysisViewModel
import com.example.racinglog.viewmodel.SimulatorViewModel

@Composable
fun AnalysisScreen(
    simulatorViewModel: SimulatorViewModel,
    analysisViewModel: AnalysisViewModel
) {
    val simulator by simulatorViewModel.selectedSimulator.collectAsState()

    val catalog = remember { RacingCatalogRepository() }

    val tracks = remember(simulator.id) {
        catalog.getTracks(simulator.id)
    }

    val cars = remember(simulator.id) {
        catalog.getCars(simulator.id)
    }

    var selectedTrackId by remember(simulator.id) {
        mutableStateOf(tracks.firstOrNull()?.id ?: "")
    }

    var selectedCarId by remember(simulator.id) {
        mutableStateOf(cars.firstOrNull()?.id ?: "")
    }

    LaunchedEffect(simulator.id) {
        analysisViewModel.loadLaps()
    }

    val analysis = analysisViewModel.getAnalysis(
        simulatorId = simulator.id,
        trackId = selectedTrackId,
        carId = selectedCarId
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column {
            Text(
                "RACING LOG / PERFORMANCE",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Análise de desempenho",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                simulator.name,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        AnalysisSection("SELECIONAR SESSÃO") {
            AnalysisDropdown(
                label = "Circuito",
                options = tracks.map { it.id to it.name },
                selectedId = selectedTrackId,
                onSelected = { selectedTrackId = it }
            )

            AnalysisDropdown(
                label = "Carro",
                options = cars.map {
                    it.id to "${it.name} (${it.category})"
                },
                selectedId = selectedCarId,
                onSelected = { selectedCarId = it }
            )

            OutlinedButton(
                onClick = { analysisViewModel.loadLaps() },
                enabled = !analysisViewModel.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ATUALIZAR ANÁLISE")
            }
        }

        if (analysisViewModel.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        analysisViewModel.errorMessage?.let { error ->
            Text(
                error,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (
            !analysisViewModel.isLoading &&
            analysisViewModel.errorMessage == null
        ) {
            if (analysis == null) {
                AnalysisSection("SEM DADOS") {
                    Text(
                        "Nenhuma volta registrada para este circuito e carro.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                AnalysisSection("RESUMO DE PERFORMANCE") {
                    Text(
                        "MELHOR VOLTA PESSOAL",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        LapTimeUtils.formatTime(analysis.bestLapMs),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AnalysisStatCard(
                            label = "VOLTA IDEAL",
                            value = analysis.idealLapMs?.let {
                                LapTimeUtils.formatTime(it)
                            } ?: "—",
                            modifier = Modifier.weight(1f)
                        )

                        AnalysisStatCard(
                            label = "GANHO POSSÍVEL",
                            value = analysis.potentialGainMs?.let {
                                "${"%.3f".format(it / 1000.0)}s"
                            } ?: "—",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Text(
                        "Voltas registradas: ${analysis.lapCount}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                AnalysisSection("MELHORES SETORES") {
                    SectorResult(
                        "SETOR 1",
                        analysis.bestSector1Ms
                    )

                    SectorResult(
                        "SETOR 2",
                        analysis.bestSector2Ms
                    )

                    SectorResult(
                        "SETOR 3",
                        analysis.bestSector3Ms
                    )

                    HorizontalDivider()

                    Text(
                        "A volta ideal combina os melhores setores " +
                                "de todas as suas voltas com este carro e circuito.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun AnalysisSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            content()
        }
    }
}

@Composable
private fun AnalysisStatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SectorResult(
    label: String,
    timeMs: Long?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            timeMs?.let {
                LapTimeUtils.formatTime(it)
            } ?: "—",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnalysisDropdown(
    label: String,
    options: List<Pair<String, String>>,
    selectedId: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedName = options
        .find { it.first == selectedId }
        ?.second ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedName,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    ExposedDropdownMenuAnchorType.PrimaryNotEditable
                ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (id, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onSelected(id)
                        expanded = false
                    }
                )
            }
        }
    }
}
