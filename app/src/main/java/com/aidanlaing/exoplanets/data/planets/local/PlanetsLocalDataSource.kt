package com.aidanlaing.exoplanets.data.planets.local

import com.aidanlaing.exoplanets.common.exceptions.MappingException
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

    override suspend fun savePlanets(planets: ArrayList<Planet>): Result<ArrayList<Planet>> = try {
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

    override suspend fun savePlanet(planet: Planet): Result<Boolean> = try {
        val mappingResult = planet.mapToResult()
        when (mappingResult) {
            is Result.Success -> {
                planetsDao.insert(mappingResult.data)
                Result.Success(true)
            }
            is Result.Failure -> Result.Failure(mappingResult.error)
        }
    } catch (exception: Exception) {
        Result.Failure(exception)
    }

    override suspend fun getFavouritePlanets(): Result<ArrayList<Planet>> = try {
        planetsDao.getFavourites()
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

    override suspend fun isFavouritePlanet(planetName: String): Result<Boolean> = try {
        val mappingResult = planetsDao.get(planetName)?.mapToResult()
        when (mappingResult) {
            is Result.Success -> Result.Success(mappingResult.data.isFavourite)
            is Result.Failure -> Result.Failure(MappingException())
            null -> Result.Failure(NotFoundException())
        }
    } catch (exception: Exception) {
        Result.Failure(exception)
    }
}