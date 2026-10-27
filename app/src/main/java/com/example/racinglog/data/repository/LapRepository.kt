package com.example.racinglog.data.repository

import com.example.racinglog.data.model.Lap
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LapRepository @Inject constructor(
    private val authRepository: AuthRepository
) {

    private val firestore = FirebaseFirestore.getInstance()

    fun saveLap(
        lap: Lap,
        onResult: (Boolean, String?) -> Unit
    ) {
        authRepository.signInAnonymously { success, error ->

            if (!success) {
                onResult(
                    false,
                    error ?: "Falha na autenticação"
                )
                return@signInAnonymously
            }

            val uid = authRepository.getCurrentUserId()

            if (uid == null) {
                onResult(false, "Usuário não autenticado")
                return@signInAnonymously
            }

            val document = firestore
                .collection("users")
                .document(uid)
                .collection("laps")
                .document()

            val lapWithId = lap.copy(
                id = document.id,
                installationId = ""
            )

            document.set(lapWithId)
                .addOnSuccessListener {
                    onResult(true, null)
                }
                .addOnFailureListener { exception ->
                    onResult(false, exception.message)
                }
        }
    }

    fun getLaps(
        onResult: (List<Lap>?, String?) -> Unit
    ) {
        authRepository.signInAnonymously { success, error ->

            if (!success) {
                onResult(null, error ?: "Falha na autenticação")
                return@signInAnonymously
            }

            val uid = authRepository.getCurrentUserId()

            if (uid == null) {
                onResult(null, "Usuário não autenticado")
                return@signInAnonymously
            }

            firestore
                .collection("users")
                .document(uid)
                .collection("laps")
                .get()
                .addOnSuccessListener { snapshot ->
                    val laps = snapshot.documents.mapNotNull {
                        it.toObject(Lap::class.java)
                    }.sortedByDescending { it.createdAt }

                    onResult(laps, null)
                }
                .addOnFailureListener { exception ->
                    onResult(null, exception.message)
                }
        }
    }

    fun deleteLap(
        lapId: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (lapId.isBlank()) {
            onResult(false, "ID da volta inválido.")
            return
        }

        val userId = authRepository.getCurrentUserId()

        if (userId == null) {
            onResult(false, "Usuário não autenticado.")
            return
        }

        firestore.collection("users")
            .document(userId)
            .collection("laps")
            .document(lapId)
            .delete()
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { exception ->
                onResult(
                    false,
                    exception.message ?: "Erro ao excluir volta."
                )
            }
    }
}

