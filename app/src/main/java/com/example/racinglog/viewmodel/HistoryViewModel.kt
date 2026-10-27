package com.example.racinglog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.racinglog.data.model.Lap
import com.example.racinglog.data.repository.LapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: LapRepository
) : ViewModel() {

    var laps by mutableStateOf<List<Lap>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set


    var isDeleting by mutableStateOf(false)
        private set

    var deleteError by mutableStateOf<String?>(null)
        private set

    fun deleteLap(lapId: String) {
        if (isDeleting) return

        isDeleting = true
        deleteError = null

        repository.deleteLap(lapId) { success, error ->
            isDeleting = false

            if (success) {
                laps = laps.filterNot { it.id == lapId }
            } else {
                deleteError = error ?: "Erro ao excluir volta."
            }
        }
    }

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
}
