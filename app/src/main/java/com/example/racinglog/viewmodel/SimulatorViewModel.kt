package com.example.racinglog.viewmodel

import androidx.lifecycle.ViewModel
import com.example.racinglog.data.model.Simulator
import com.example.racinglog.data.repository.SimulatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SimulatorViewModel @Inject constructor(
    repository: SimulatorRepository
) : ViewModel() {

    val simulators: List<Simulator> =
        repository.getSimulators()

    private val _selectedSimulator =
        MutableStateFlow(simulators.first())

    val selectedSimulator: StateFlow<Simulator> =
        _selectedSimulator.asStateFlow()

    fun selectSimulator(simulator: Simulator) {
        _selectedSimulator.value = simulator
    }
}

