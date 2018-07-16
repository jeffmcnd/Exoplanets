package com.aidanlaing.exoplanets.data.confirmedplanets

interface ConfirmedPlanetsDataSource {
    suspend fun getConfirmedPlanets(): ArrayList<ConfirmedPlanet>
    suspend fun getConfirmedPlanet(planetName: String): ConfirmedPlanet?
    suspend fun saveConfirmedPlanets(confirmedPlanets: ArrayList<ConfirmedPlanet>)
}