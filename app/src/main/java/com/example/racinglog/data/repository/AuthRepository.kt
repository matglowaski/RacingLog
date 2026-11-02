package com.example.racinglog.data.repository

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() {

    private val auth = FirebaseAuth.getInstance()

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null &&
                auth.currentUser?.isAnonymous == false
    }

    fun createAccount(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onResult(false, "Preencha o e-mail e a senha.")
            return
        }

        if (password.length < 6) {
            onResult(false, "A senha deve ter pelo menos 6 caracteres.")
            return
        }

        auth.createUserWithEmailAndPassword(
            email.trim(),
            password
        )
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { exception ->
                onResult(false, exception.message)
            }
    }

    fun signIn(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onResult(false, "Preencha o e-mail e a senha.")
            return
        }

        auth.signInWithEmailAndPassword(
            email.trim(),
            password
        )
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { exception ->
                onResult(false, exception.message)
            }
    }

    fun signOut() {
        auth.signOut()
    }
}
