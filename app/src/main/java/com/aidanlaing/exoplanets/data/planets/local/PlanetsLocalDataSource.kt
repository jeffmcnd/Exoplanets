package com.aidanlaing.exoplanets.data.planets.local

import com.aidanlaing.exoplanets.common.exceptions.NotFoundException
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.data.planets.PlanetsDataSource

class PlanetsLocalDataSource
private constructor(
        private val planetsDao: PlanetsDao
) : PlanetsDataSource {

    companion object {

        @Volatile
        private var INSTANCE: PlanetsLocalDataSource? = null

        fun getInstance(
                planetsDao: PlanetsDao
        ): PlanetsLocalDataSource = INSTANCE ?: synchronized(this) {
            INSTANCE ?: PlanetsLocalDataSource(planetsDao)
                    .also { INSTANCE = it }
        }
    }

    override suspend fun getPlanets(): Result<ArrayList<Planet>> = try {
        planetsDao.get()
                .mapNotNullTo(ArrayList()) { planetLocal ->
                    val mappingResult = planetLocal.mapToResult()
                    when (mappingResult) {
                        is Result.Success -> mappingResult.data
                        is Result.Failure -> null
                    }
                }
                .let { list -> Result.Success(list) }

    } catch (exception: Exception) {
        Result.Failure(exception)
    }

    override suspend fun getPlanet(name: String): Result<Planet> = try {
        planetsDao.get(name)?.mapToResult() ?: Result.Failure(NotFoundException())
    } catch (exception: Exception) {
        Result.Failure(exception)
    }

    override suspend fun savePlanets(
            planets: ArrayList<Planet>
    ): Result<ArrayList<Planet>> = try {
        val localPlanets = planets.mapNotNull { planet ->
            val mappingResult = planet.mapToResult()
            when (mappingResult) {
                is Result.Success -> mappingResult.data
                is Result.Failure -> null
            }
        }
        planetsDao.insert(localPlanets)
        Result.Success(planets)

    } catch (exception: Exception) {
        Result.Failure(exception)
    }
}