package com.example.racinglog.data.model

data class LapAnalysis(
    val lapCount: Int,
    val bestLapMs: Long,
    val idealLapMs: Long?,
    val potentialGainMs: Long?,
    val bestSector1Ms: Long?,
    val bestSector2Ms: Long?,
    val bestSector3Ms: Long?
)