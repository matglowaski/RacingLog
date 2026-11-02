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
        val uid = authRepository.getCurrentUserId()

        if (uid == null || !authRepository.isUserLoggedIn()) {
            onResult(false, "Faça login para registrar uma volta.")
            return
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
                onResult(false, exception.message ?: "Erro ao salvar volta.")
            }
    }

    fun getLaps(
        onResult: (List<Lap>?, String?) -> Unit
    ) {
        val uid = authRepository.getCurrentUserId()

        if (uid == null || !authRepository.isUserLoggedIn()) {
            onResult(null, "Faça login para visualizar seu histórico.")
            return
        }

        firestore.collection("users")
            .document(uid)
            .collection("laps")
            .get()
            .addOnSuccessListener { snapshot ->
                val laps = snapshot.documents
                    .mapNotNull { it.toObject(Lap::class.java) }
                    .sortedByDescending { it.createdAt }

                onResult(laps, null)
            }
            .addOnFailureListener { exception ->
                onResult(
                    null,
                    exception.message ?: "Erro ao carregar histórico."
                )
            }
    }

    fun updateLap(
        lap: Lap,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (lap.id.isBlank()) {
            onResult(false, "ID da volta inválido.")
            return
        }

        val userId = authRepository.getCurrentUserId()

        if (userId == null || !authRepository.isUserLoggedIn()) {
            onResult(false, "Faça login para editar uma volta.")
            return
        }

        firestore.collection("users")
            .document(userId)
            .collection("laps")
            .document(lap.id)
            .set(lap.copy(installationId = ""))
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { exception ->
                onResult(
                    false,
                    exception.message ?: "Erro ao atualizar volta."
                )
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

        if (userId == null || !authRepository.isUserLoggedIn()) {
            onResult(false, "Faça login para excluir uma volta.")
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
