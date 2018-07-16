package com.aidanlaing.exoplanets.data.confirmedplanets

interface ConfirmedPlanetsDataSource {
    suspend fun getConfirmedPlanets(refresh: Boolean): ArrayList<ConfirmedPlanet>
    suspend fun saveConfirmedPlanets(confirmedPlanets: ArrayList<ConfirmedPlanet>)
}