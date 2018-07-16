package com.aidanlaing.exoplanets.data.confirmedplanets

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

    override suspend fun getConfirmedPlanets(): ArrayList<ConfirmedPlanet> {
        return confirmedPlanetsDao.get()
                .mapTo(ArrayList()) { confirmedPlanetLocal ->
                    ConfirmedPlanet.from(confirmedPlanetLocal)
                }
    }

    override suspend fun saveConfirmedPlanets(confirmedPlanets: ArrayList<ConfirmedPlanet>) {
        val localConfirmedPlanets = confirmedPlanets.map { confirmedPlanet ->
            ConfirmedPlanetLocal.from(confirmedPlanet)
        }
        confirmedPlanetsDao.insert(localConfirmedPlanets)
    }
}