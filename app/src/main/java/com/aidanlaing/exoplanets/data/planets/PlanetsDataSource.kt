package com.aidanlaing.exoplanets.data.planets

import com.aidanlaing.exoplanets.data.Result

interface PlanetsDataSource {
    suspend fun getPlanets(): Result<ArrayList<Planet>>
    suspend fun getPlanet(name: String): Result<Planet>
    suspend fun savePlanets(planets: ArrayList<Planet>): Result<ArrayList<Planet>>
    suspend fun savePlanet(planet: Planet): Result<Boolean>

    suspend fun getFavouritePlanets(): Result<ArrayList<Planet>>
    suspend fun isFavouritePlanet(planetName: String): Result<Boolean>
}