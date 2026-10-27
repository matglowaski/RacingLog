package com.example.racinglog.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.racinglog.data.model.Simulator
import com.example.racinglog.viewmodel.SimulatorViewModel

@Composable
fun HomeScreen(
    simulatorViewModel: SimulatorViewModel
) {
    val selectedSimulator by
    simulatorViewModel.selectedSimulator.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "RACING LOG",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Sua garagem",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Selecione seu simulador para começar.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "SIMULADOR ATIVO",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = selectedSimulator.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Pronto para registrar suas voltas.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "Escolha seu simulador",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        simulatorViewModel.simulators
            .chunked(2)
            .forEach { rowSimulators ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowSimulators.forEach { simulator ->
                        SimulatorCard(
                            simulator = simulator,
                            selected = selectedSimulator.id == simulator.id,
                            onClick = {
                                simulatorViewModel.selectSimulator(simulator)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (rowSimulators.size == 1) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
    }
}

@Composable
private fun SimulatorCard(
    simulator: Simulator,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shortName = when (simulator.id) {
        "lmu" -> "LMU"
        "ams2" -> "AMS2"
        "iracing" -> "iR"
        "acc" -> "ACC"
        else -> "SIM"
    }

    val subtitle = when (simulator.id) {
        "lmu" -> "Endurance"
        "ams2" -> "Motorsport"
        "iracing" -> "Sim Racing"
        "acc" -> "GT Racing"
        else -> "Simulator"
    }

    Card(
        modifier = modifier
            .height(150.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = shortName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Column {
                Text(
                    text = simulator.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )

                Text(
                    text = if (selected) "SELECIONADO" else subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}
