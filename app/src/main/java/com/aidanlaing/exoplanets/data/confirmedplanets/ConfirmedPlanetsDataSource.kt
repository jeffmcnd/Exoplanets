package com.aidanlaing.exoplanets.data.confirmedplanets

import com.aidanlaing.exoplanets.data.Result

interface ConfirmedPlanetsDataSource {
    suspend fun getConfirmedPlanets(): Result<ArrayList<ConfirmedPlanet>>
    suspend fun getConfirmedPlanet(planetName: String): Result<ConfirmedPlanet>
    suspend fun saveConfirmedPlanets(confirmedPlanets: ArrayList<ConfirmedPlanet>): Result<ArrayList<ConfirmedPlanet>>
}