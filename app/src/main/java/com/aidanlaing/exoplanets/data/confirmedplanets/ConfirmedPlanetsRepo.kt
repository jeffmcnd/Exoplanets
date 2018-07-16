package com.aidanlaing.exoplanets.data.confirmedplanets

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

    override suspend fun getConfirmedPlanets(): ArrayList<ConfirmedPlanet> {
        val localConfirmedPlanets = localDataSource.getConfirmedPlanets()
        if (localConfirmedPlanets.isNotEmpty()) {
            cacheConfirmedPlanets(localConfirmedPlanets)
            return localConfirmedPlanets
        }

        val remoteConfirmedPlanets = remoteDataSource.getConfirmedPlanets()
        cacheConfirmedPlanets(remoteConfirmedPlanets)
        saveConfirmedPlanets(remoteConfirmedPlanets)

        return remoteConfirmedPlanets
    }

    override suspend fun saveConfirmedPlanets(confirmedPlanets: ArrayList<ConfirmedPlanet>) {
        localDataSource.saveConfirmedPlanets(confirmedPlanets)
    }
}