package com.aidanlaing.exoplanets.data.confirmedplanets.local

import com.aidanlaing.exoplanets.common.exceptions.NotFoundException
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanet
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsDataSource

class ConfirmedPlanetsLocalDataSource
private constructor(
        private val confirmedPlanetsDao: ConfirmedPlanetsDao
) : ConfirmedPlanetsDataSource {

    companion object {

        @Volatile
        private var INSTANCE: ConfirmedPlanetsLocalDataSource? = null

        fun getInstance(
                confirmedPlanetsDao: ConfirmedPlanetsDao
        ): ConfirmedPlanetsLocalDataSource = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ConfirmedPlanetsLocalDataSource(confirmedPlanetsDao)
                    .also { INSTANCE = it }
        }
    }

    override suspend fun getConfirmedPlanets(): Result<ArrayList<ConfirmedPlanet>> = try {
        confirmedPlanetsDao.get()
                .mapNotNullTo(ArrayList()) { confirmedPlanetLocal ->
                    val mappingResult = confirmedPlanetLocal.mapToResult()
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
        confirmedPlanetsDao.get(planetName)?.mapToResult() ?: Result.Failure(NotFoundException())
    } catch (exception: Exception) {
        Result.Failure(exception)
    }

    override suspend fun saveConfirmedPlanets(
            confirmedPlanets: ArrayList<ConfirmedPlanet>
    ): Result<ArrayList<ConfirmedPlanet>> = try {
        val localConfirmedPlanets = confirmedPlanets.mapNotNull { confirmedPlanet ->
            val mappingResult = confirmedPlanet.mapToResult()
            when (mappingResult) {
                is Result.Success -> mappingResult.data
                is Result.Failure -> null
            }
        }
        confirmedPlanetsDao.insert(localConfirmedPlanets)
        Result.Success(confirmedPlanets)

    } catch (exception: Exception) {
        Result.Failure(exception)
    }
}