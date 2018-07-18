package com.aidanlaing.exoplanets.data.confirmedplanets.remote

import com.aidanlaing.exoplanets.common.exceptions.InvalidOperationException
import com.aidanlaing.exoplanets.data.Result
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
        ): ConfirmedPlanetsRemoteDataSource = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ConfirmedPlanetsRemoteDataSource(confirmedPlanetsApi)
                    .also { INSTANCE = it }
        }
    }

    override suspend fun getConfirmedPlanets(): Result<ArrayList<ConfirmedPlanet>> = try {
        confirmedPlanetsApi.get()
                .await()
                .mapNotNullTo(ArrayList()) { confirmedPlanetRemote ->
                    val mappingResult = confirmedPlanetRemote.mapToResult()
                    when (mappingResult) {
                        is Result.Success -> mappingResult.data
                        is Result.Failure -> null
                    }
                }
                .let { list -> Result.Success(list) }

    } catch (exception: Exception) {
        Result.Failure(exception)
    }

    override suspend fun getConfirmedPlanet(planetName: String): Result<ConfirmedPlanet> = try {
        confirmedPlanetsApi.get(where = "pl_name like '$planetName'")
                .await()
                .mapNotNull { confirmedPlanetRemote ->
                    val mappingResult = confirmedPlanetRemote.mapToResult()
                    when (mappingResult) {
                        is Result.Success -> mappingResult.data
                        is Result.Failure -> null
                    }
                }
                .first()
                .let { confirmedPlanet -> Result.Success(confirmedPlanet) }

    } catch (exception: Exception) {
        Result.Failure(exception)
    }

    override suspend fun saveConfirmedPlanets(confirmedPlanets: ArrayList<ConfirmedPlanet>) =
            Result.Failure(InvalidOperationException())
}