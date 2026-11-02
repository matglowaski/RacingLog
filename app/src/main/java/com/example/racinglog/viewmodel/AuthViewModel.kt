package com.example.racinglog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.racinglog.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var isLoggedIn by mutableStateOf(repository.isUserLoggedIn())
        private set

    var message by mutableStateOf<String?>(null)
        private set

    fun clearMessage() {
        message = null
    }

    fun signIn(email: String, password: String) {
        if (isLoading) return

        isLoading = true
        message = null

        repository.signIn(email, password) { success, error ->
            isLoading = false

            if (success) {
                isLoggedIn = true
            } else {
                message = error ?: "Não foi possível entrar."
            }
        }
    }

    fun createAccount(email: String, password: String) {
        if (isLoading) return

        isLoading = true
        message = null

        repository.createAccount(email, password) { success, error ->
            isLoading = false

            if (success) {
                isLoggedIn = true
            } else {
                message = error ?: "Não foi possível criar a conta."
            }
        }
    }

    fun signOut() {
        repository.signOut()
        isLoggedIn = false
        message = null
    }
}
