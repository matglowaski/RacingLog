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
class LapViewModel @Inject constructor(
    private val repository: LapRepository
) : ViewModel() {

    var isSaving by mutableStateOf(false)
        private set

    var message by mutableStateOf<String?>(null)
        private set

    var saveSucceeded by mutableStateOf(false)
        private set

    var lapBeingEdited by mutableStateOf<Lap?>(null)
        private set

    var isUpdating by mutableStateOf(false)
        private set

    var updateSucceeded by mutableStateOf(false)
        private set

    fun setLapToEdit(lap: Lap) {
        lapBeingEdited = lap
        message = null
        updateSucceeded = false
    }

    fun clearEditingLap() {
        lapBeingEdited = null
        message = null
        updateSucceeded = false
    }

    fun clearMessage() {
        message = null
        saveSucceeded = false
        updateSucceeded = false
    }

    fun saveLap(lap: Lap) {
        if (isSaving) return

        isSaving = true
        message = null
        saveSucceeded = false

        repository.saveLap(lap) { success, error ->
            isSaving = false
            saveSucceeded = success

            message = if (success) {
                "Volta salva com sucesso!"
            } else {
                "Erro ao salvar: ${error ?: "Erro desconhecido"}"
            }
        }
    }

    fun updateLap(lap: Lap) {
        if (isUpdating) return

        isUpdating = true
        message = null
        updateSucceeded = false

        repository.updateLap(lap) { success, error ->
            isUpdating = false
            updateSucceeded = success

            message = if (success) {
                "Volta atualizada com sucesso!"
            } else {
                "Erro ao atualizar: ${error ?: "Erro desconhecido"}"
            }
        }
    }
}