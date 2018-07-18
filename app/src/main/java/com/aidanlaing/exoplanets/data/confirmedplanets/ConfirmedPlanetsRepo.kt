package com.aidanlaing.exoplanets.data.confirmedplanets

import com.aidanlaing.exoplanets.data.Result

class ConfirmedPlanetsRepo
private constructor(
        private val remoteDataSource: ConfirmedPlanetsDataSource,
        private val localDataSource: ConfirmedPlanetsDataSource
) : ConfirmedPlanetsDataSource {

    companion object {

        @Volatile
        private var INSTANCE: ConfirmedPlanetsRepo? = null

        fun getInstance(
                remoteDataSource: ConfirmedPlanetsDataSource,
                localDataSource: ConfirmedPlanetsDataSource
        ): ConfirmedPlanetsRepo = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ConfirmedPlanetsRepo(remoteDataSource, localDataSource)
                    .also { INSTANCE = it }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    private val cachedConfirmedPlanets = LinkedHashMap<String, ConfirmedPlanet>()

    private fun cacheConfirmedPlanets(confirmedPlanets: ArrayList<ConfirmedPlanet>) {
        confirmedPlanets.forEach { confirmedPlanet -> cacheConfirmedPlanet(confirmedPlanet) }
    }

    private fun cacheConfirmedPlanet(confirmedPlanet: ConfirmedPlanet) {
        cachedConfirmedPlanets[confirmedPlanet.planetName] = confirmedPlanet
    }

    override suspend fun getConfirmedPlanets(): Result<ArrayList<ConfirmedPlanet>> {
        val cached = cachedConfirmedPlanets.values
        if (cached.isNotEmpty()) {
            return Result.Success(ArrayList(cached))
        }

        val localResult = localDataSource.getConfirmedPlanets()
        when (localResult) {
            is Result.Success -> {
                val data = localResult.data
                if (data.isNotEmpty()) {
                    cacheConfirmedPlanets(data)
                    return localResult
                }
            }
        }

        val remoteResult = remoteDataSource.getConfirmedPlanets()
        return when (remoteResult) {
            is Result.Success -> {
                val data = remoteResult.data
                cacheConfirmedPlanets(data)
                saveConfirmedPlanets(data)
                remoteResult
            }
            is Result.Failure -> remoteResult
        }
    }

    override suspend fun getConfirmedPlanet(planetName: String): Result<ConfirmedPlanet> {
        val cached = cachedConfirmedPlanets[planetName]
        if (cached != null) return Result.Success(cached)

        val localResult = localDataSource.getConfirmedPlanet(planetName)
        when (localResult) {
            is Result.Success -> {
                cacheConfirmedPlanet(localResult.data)
                return localResult
            }
        }

        val remoteResult = remoteDataSource.getConfirmedPlanet(planetName)
        return when (remoteResult) {
            is Result.Success -> {
                cacheConfirmedPlanet(remoteResult.data)
                remoteResult
            }
            is Result.Failure -> remoteResult
        }
    }

    override suspend fun saveConfirmedPlanets(
            confirmedPlanets: ArrayList<ConfirmedPlanet>
    ): Result<ArrayList<ConfirmedPlanet>> = localDataSource.saveConfirmedPlanets(confirmedPlanets)
}