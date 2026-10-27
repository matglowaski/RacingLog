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

    fun clearMessage() {
        message = null
        saveSucceeded = false
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
}


