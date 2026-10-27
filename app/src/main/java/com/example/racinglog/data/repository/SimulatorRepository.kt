package com.example.racinglog.data.repository

import com.example.racinglog.data.model.Simulator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SimulatorRepository @Inject constructor() {

    fun getSimulators(): List<Simulator> {
        return listOf(
            Simulator("lmu", "Le Mans Ultimate"),
            Simulator("ams2", "Automobilista 2"),
            Simulator("iracing", "iRacing"),
            Simulator("acc", "Assetto Corsa Competizione")
        )
    }
}