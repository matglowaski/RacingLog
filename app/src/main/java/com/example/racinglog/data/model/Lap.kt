package com.example.racinglog.data.model

data class Lap(
    val id: String = "",
    val installationId: String = "",
    val simulatorId: String = "",
    val trackId: String = "",
    val carId: String = "",
    val category: String = "",
    val totalTimeMs: Long = 0L,
    val sector1Ms: Long? = null,
    val sector2Ms: Long? = null,
    val sector3Ms: Long? = null,
    val createdAt: Long = 0L
)
