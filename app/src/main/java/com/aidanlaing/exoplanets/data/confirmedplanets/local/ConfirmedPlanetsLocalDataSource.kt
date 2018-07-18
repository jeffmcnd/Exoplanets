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
                .mapTo(ArrayList()) { confirmedPlanetLocal ->
                    ConfirmedPlanet.from(confirmedPlanetLocal)
                }
                .let { list -> Result.Success(list) }

    } catch (exception: Exception) {
        Result.Failure(exception)
    }

    override suspend fun getConfirmedPlanet(planetName: String): Result<ConfirmedPlanet> = try {
        val confirmedPlanetLocal = confirmedPlanetsDao.get(planetName)
        if (confirmedPlanetLocal == null) Result.Failure(NotFoundException())
        else Result.Success(ConfirmedPlanet.from(confirmedPlanetLocal))

    } catch (exception: Exception) {
        Result.Failure(exception)
    }

    override suspend fun saveConfirmedPlanets(
            confirmedPlanets: ArrayList<ConfirmedPlanet>
    ): Result<ArrayList<ConfirmedPlanet>> = try {
        val localConfirmedPlanets = confirmedPlanets.map { confirmedPlanet ->
            ConfirmedPlanetLocal.from(confirmedPlanet)
        }
        confirmedPlanetsDao.insert(localConfirmedPlanets)
        Result.Success(confirmedPlanets)

    } catch (exception: Exception) {
        Result.Failure(exception)
    }
}