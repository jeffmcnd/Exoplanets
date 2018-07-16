package com.aidanlaing.exoplanets.data.confirmedplanets.remote

import com.aidanlaing.exoplanets.common.exceptions.InvalidOperationException
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanet
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsDataSource

class ConfirmedPlanetsRemoteDataSource
private constructor(
        private val confirmedPlanetsApi: ConfirmedPlanetsApi
) : ConfirmedPlanetsDataSource {

    companion object {

        @Volatile
        private var INSTANCE: ConfirmedPlanetsRemoteDataSource? = null

        fun getInstance(
                confirmedPlanetsApi: ConfirmedPlanetsApi
        ): ConfirmedPlanetsRemoteDataSource = INSTANCE
                ?: synchronized(this) {
            INSTANCE
                    ?: ConfirmedPlanetsRemoteDataSource(confirmedPlanetsApi)
                    .also { INSTANCE = it }
        }
    }

    override suspend fun getConfirmedPlanets(): ArrayList<ConfirmedPlanet> {
        return confirmedPlanetsApi.get()
                .await()
                .map { confirmedPlanetRemote ->
                    ConfirmedPlanet.fromOrNull(confirmedPlanetRemote)
                }
                .filterNotNullTo(ArrayList())
    }

    override suspend fun getConfirmedPlanet(planetName: String): ConfirmedPlanet? {
        return confirmedPlanetsApi.get(where = "pl_name like '$planetName'")
                .await()
                .map { confirmedPlanetRemote ->
                    ConfirmedPlanet.fromOrNull(confirmedPlanetRemote)
                }
                .filterNotNull()
                .firstOrNull()
    }

    override suspend fun saveConfirmedPlanets(confirmedPlanets: ArrayList<ConfirmedPlanet>) =
            throw InvalidOperationException()
}