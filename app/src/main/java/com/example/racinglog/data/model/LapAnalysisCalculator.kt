package com.example.racinglog.data.model

object LapAnalysisCalculator {

    fun calculate(laps: List<Lap>): LapAnalysis? {
        if (laps.isEmpty()) return null

        val bestLap = laps.minOf { it.totalTimeMs }

        val bestS1 = laps.mapNotNull { it.sector1Ms }
            .minOrNull()

        val bestS2 = laps.mapNotNull { it.sector2Ms }
            .minOrNull()

        val bestS3 = laps.mapNotNull { it.sector3Ms }
            .minOrNull()

        val idealLap = if (
            bestS1 != null &&
            bestS2 != null &&
            bestS3 != null
        ) {
            bestS1 + bestS2 + bestS3
        } else {
            null
        }

        val potentialGain = idealLap?.let {
            (bestLap - it).coerceAtLeast(0L)
        }

        return LapAnalysis(
            lapCount = laps.size,
            bestLapMs = bestLap,
            idealLapMs = idealLap,
            potentialGainMs = potentialGain,
            bestSector1Ms = bestS1,
            bestSector2Ms = bestS2,
            bestSector3Ms = bestS3
        )
    }
}
