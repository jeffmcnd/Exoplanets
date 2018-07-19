package com.aidanlaing.exoplanets.data.planets.remote

import com.aidanlaing.exoplanets.common.exceptions.InvalidOperationException
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.data.planets.PlanetsDataSource

class PlanetsRemoteDataSource
private constructor(
        private val planetsApi: PlanetsApi
) : PlanetsDataSource {

    companion object {

        @Volatile
        private var INSTANCE: PlanetsRemoteDataSource? = null

        fun getInstance(
                planetsApi: PlanetsApi
        ): PlanetsRemoteDataSource = INSTANCE ?: synchronized(this) {
            INSTANCE ?: PlanetsRemoteDataSource(planetsApi)
                    .also { INSTANCE = it }
        }
    }

    override suspend fun getPlanets(): Result<ArrayList<Planet>> = try {
        planetsApi.get()
                .await()
                .mapNotNullTo(ArrayList()) { planetRemote ->
                    val mappingResult = planetRemote.mapToResult()
                    when (mappingResult) {
                        is Result.Success -> mappingResult.data
                        is Result.Failure -> null
                    }
                }
                .let { list -> Result.Success(list) }

    } catch (exception: Exception) {
        Result.Failure(exception)
    }

    override suspend fun getPlanet(planetName: String): Result<Planet> = try {
        planetsApi.get(where = "pl_name like '$planetName'")
                .await()
                .mapNotNull { planetRemote ->
                    val mappingResult = planetRemote.mapToResult()
                    when (mappingResult) {
                        is Result.Success -> mappingResult.data
                        is Result.Failure -> null
                    }
                }
                .first()
                .let { planet -> Result.Success(planet) }

    } catch (exception: Exception) {
        Result.Failure(exception)
    }

    override suspend fun savePlanets(planets: ArrayList<Planet>) =
            Result.Failure(InvalidOperationException())
}