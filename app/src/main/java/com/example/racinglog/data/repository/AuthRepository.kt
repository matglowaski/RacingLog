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

    fun signInAnonymously(
        onResult: (Boolean, String?) -> Unit
    ) {
        if (auth.currentUser != null) {
            onResult(true, null)
            return
        }

        auth.signInAnonymously()
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { exception ->
                onResult(false, exception.message)
            }
    }
}
