package com.example.racinglog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.racinglog.data.model.Lap
import com.example.racinglog.data.model.LapAnalysis
import com.example.racinglog.data.model.LapAnalysisCalculator
import com.example.racinglog.data.repository.LapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val repository: LapRepository
) : ViewModel() {

    var laps by mutableStateOf<List<Lap>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadLaps() {
        if (isLoading) return

        isLoading = true
        errorMessage = null

        repository.getLaps { result, error ->
            isLoading = false

            if (result != null) {
                laps = result
            } else {
                errorMessage = error ?: "Erro ao carregar voltas."
            }
        }
    }

    fun getAnalysis(
        simulatorId: String,
        trackId: String,
        carId: String
    ): LapAnalysis? {
        val filteredLaps = laps.filter {
            it.simulatorId == simulatorId &&
                    it.trackId == trackId &&
                    it.carId == carId
        }

        return LapAnalysisCalculator.calculate(filteredLaps)
    }
}
