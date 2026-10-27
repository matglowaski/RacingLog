package com.example.racinglog.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.racinglog.data.model.Lap
import com.example.racinglog.data.model.LapTimeUtils
import com.example.racinglog.data.repository.RacingCatalogRepository
import com.example.racinglog.viewmodel.LapViewModel
import com.example.racinglog.viewmodel.SimulatorViewModel

@Composable
fun RegisterScreen(
    simulatorViewModel: SimulatorViewModel,
    lapViewModel: LapViewModel
) {
    val simulator by simulatorViewModel.selectedSimulator.collectAsState()

    val catalog = remember { RacingCatalogRepository() }

    val tracks = remember(simulator.id) {
        catalog.getTracks(simulator.id)
    }

    val cars = remember(simulator.id) {
        catalog.getCars(simulator.id)
    }

    val categories = remember(cars) {
        cars.map { it.category }.distinct()
    }

    var selectedTrack by remember(simulator.id) {
        mutableStateOf(tracks.firstOrNull())
    }

    var selectedCategory by remember(simulator.id) {
        mutableStateOf(categories.firstOrNull() ?: "")
    }

    val availableCars = cars.filter {
        it.category == selectedCategory
    }

    var selectedCar by remember(simulator.id, selectedCategory) {
        mutableStateOf(availableCars.firstOrNull())
    }

    var totalTime by remember { mutableStateOf("") }
    var sector1 by remember { mutableStateOf("") }
    var sector2 by remember { mutableStateOf("") }
    var sector3 by remember { mutableStateOf("") }

    val totalMs = LapTimeUtils.parseTime(totalTime)
    val s1 = if (sector1.isBlank()) null
    else LapTimeUtils.parseTime(sector1)
    val s2 = if (sector2.isBlank()) null
    else LapTimeUtils.parseTime(sector2)
    val s3 = if (sector3.isBlank()) null
    else LapTimeUtils.parseTime(sector3)

    val sectorsFilled = listOf(
        sector1, sector2, sector3
    ).count { it.isNotBlank() }

    val sectorsValid = when (sectorsFilled) {
        0 -> true
        3 -> s1 != null && s2 != null && s3 != null
        else -> false
    }

    val sectorSum = if (s1 != null && s2 != null && s3 != null) {
        try {
            Math.addExact(Math.addExact(s1, s2), s3)
        } catch (e: ArithmeticException) {
            null
        }
    } else {
        null
    }

    val timesMatch = sectorSum == null ||
            sectorSum == totalMs

    val canSave = selectedTrack != null &&
            selectedCar != null &&
            totalMs != null &&
            sectorsValid &&
            timesMatch &&
            !lapViewModel.isSaving

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column {
            Text(
                "RACING LOG / NOVA VOLTA",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Registrar volta",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                simulator.name,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        RacingSection(
            title = "CONFIGURAÇÃO DA SESSÃO"
        ) {
            RacingDropdown(
                label = "Circuito",
                options = tracks.map { it.id to it.name },
                selectedId = selectedTrack?.id ?: "",
                onSelected = { id ->
                    selectedTrack = tracks.find { it.id == id }
                }
            )

            RacingDropdown(
                label = "Categoria",
                options = categories.map { it to it },
                selectedId = selectedCategory,
                onSelected = {
                    selectedCategory = it
                }
            )

            RacingDropdown(
                label = "Carro",
                options = availableCars.map { it.id to it.name },
                selectedId = selectedCar?.id ?: "",
                onSelected = { id ->
                    selectedCar = availableCars.find { it.id == id }
                }
            )
        }

        RacingSection(
            title = "CRONOMETRAGEM"
        ) {
            OutlinedTextField(
                value = totalTime,
                onValueChange = { totalTime = it },
                label = { Text("Tempo total") },
                placeholder = { Text("2:01.250") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Text(
                "Formato: m:ss.mmm ou ss.mmm",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                "Setores (opcional)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SectorField(
                    label = "Setor 1",
                    value = sector1,
                    onValueChange = { sector1 = it },
                    modifier = Modifier.weight(1f)
                )

                SectorField(
                    label = "Setor 2",
                    value = sector2,
                    onValueChange = { sector2 = it },
                    modifier = Modifier.weight(1f)
                )

                SectorField(
                    label = "Setor 3",
                    value = sector3,
                    onValueChange = { sector3 = it },
                    modifier = Modifier.weight(1f)
                )
            }

            if (!sectorsValid) {
                Text(
                    "Preencha os três setores corretamente ou deixe todos vazios.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            } else if (!timesMatch) {
                Text(
                    "A soma dos setores deve ser igual ao tempo total.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Button(
            onClick = {
                val lap = Lap(
                    simulatorId = simulator.id,
                    trackId = selectedTrack!!.id,
                    carId = selectedCar!!.id,
                    category = selectedCar!!.category,
                    totalTimeMs = totalMs!!,
                    sector1Ms = s1,
                    sector2Ms = s2,
                    sector3Ms = s3,
                    createdAt = System.currentTimeMillis()
                )

                lapViewModel.saveLap(lap)
            },
            enabled = canSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                if (lapViewModel.isSaving) {
                    "SALVANDO..."
                } else {
                    "SALVAR VOLTA"
                },
                fontWeight = FontWeight.Bold
            )
        }

        lapViewModel.message?.let { message ->
            Text(
                message,
                color = if (lapViewModel.saveSucceeded) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun RacingSection(
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
private fun SectorField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text("42.350") },
        modifier = modifier,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodySmall,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RacingDropdown(
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
