package com.aidanlaing.exoplanets.data.planets

import com.aidanlaing.exoplanets.data.Result

class PlanetsRepo
private constructor(
        private val remoteDataSource: PlanetsDataSource,
        private val localDataSource: PlanetsDataSource
) : PlanetsDataSource {

    companion object {

        @Volatile
        private var INSTANCE: PlanetsRepo? = null

        fun getInstance(
                remoteDataSource: PlanetsDataSource,
                localDataSource: PlanetsDataSource
        ): PlanetsRepo = INSTANCE ?: synchronized(this) {
            INSTANCE ?: PlanetsRepo(remoteDataSource, localDataSource)
                    .also { INSTANCE = it }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    private val cachedPlanets = LinkedHashMap<String, Planet>()

    private fun cachePlanets(planets: ArrayList<Planet>) {
        planets.forEach { planet -> cachePlanet(planet) }
    }

    private fun cachePlanet(planet: Planet) {
        cachedPlanets[planet.name] = planet
    }

    override suspend fun getPlanets(): Result<ArrayList<Planet>> {
        val cached = cachedPlanets.values
        if (cached.isNotEmpty()) {
            return Result.Success(ArrayList(cached))
        }

        val localResult = localDataSource.getPlanets()
        if (localResult is Result.Success) {
            val data = localResult.data
            if (data.isNotEmpty()) {
                cachePlanets(data)
                return localResult
            }
        }

        val remoteResult = remoteDataSource.getPlanets()
        return when (remoteResult) {
            is Result.Success -> {
                val data = remoteResult.data
                cachePlanets(data)
                savePlanets(data)
                remoteResult
            }
            is Result.Failure -> remoteResult
        }
    }

    override suspend fun getPlanet(name: String): Result<Planet> {
        val cached = cachedPlanets[name]
        if (cached != null) return Result.Success(cached)

        val localResult = localDataSource.getPlanet(name)
        if (localResult is Result.Success) {
            cachePlanet(localResult.data)
            return localResult
        }

        val remoteResult = remoteDataSource.getPlanet(name)
        return when (remoteResult) {
            is Result.Success -> {
                cachePlanet(remoteResult.data)
                remoteResult
            }
            is Result.Failure -> remoteResult
        }
    }

    override suspend fun savePlanets(planets: ArrayList<Planet>): Result<ArrayList<Planet>> =
            localDataSource.savePlanets(planets)

    override suspend fun savePlanet(planet: Planet): Result<Boolean> =
            localDataSource.savePlanet(planet)

    override suspend fun getFavouritePlanets(): Result<ArrayList<Planet>> =
            localDataSource.getFavouritePlanets()

    override suspend fun isFavouritePlanet(planetName: String): Result<Boolean> =
            localDataSource.isFavouritePlanet(planetName)
}