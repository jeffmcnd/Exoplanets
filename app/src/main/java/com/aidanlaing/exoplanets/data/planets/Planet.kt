package com.aidanlaing.exoplanets.data.planets

import com.aidanlaing.exoplanets.data.Mappable
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.local.PlanetLocal

data class Planet(
        val planetName: String,
        val hostStarName: String?,
        val planetLetter: String?,
        val discoveryMethod: String?,
        val numPlanetsInSystem: Int?,
        val orbitalPeriodDays: Double?,
        val planetJupiterMass: Double?,
        val planetJupiterRadius: Double?,
        val planetDensity: Double?
) : Mappable<PlanetLocal> {

    override fun mapToResult(): Result<PlanetLocal> {
        val localPlanet = PlanetLocal(
                planetName,
                hostStarName,
                planetLetter,
                discoveryMethod,
                numPlanetsInSystem,
                orbitalPeriodDays,
                planetJupiterMass,
                planetJupiterRadius,
                planetDensity
        )
        return Result.Success(localPlanet)
    }

    fun getPlanetImage() = PlanetImage.from(this)
}