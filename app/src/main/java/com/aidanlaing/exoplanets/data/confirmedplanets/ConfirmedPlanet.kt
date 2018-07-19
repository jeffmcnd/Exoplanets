package com.aidanlaing.exoplanets.data.confirmedplanets

import com.aidanlaing.exoplanets.data.Mappable
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.confirmedplanets.local.ConfirmedPlanetLocal

data class ConfirmedPlanet(
        val planetName: String,
        val hostStarName: String?,
        val planetLetter: String?,
        val discoveryMethod: String?,
        val numPlanetsInSystem: Int?,
        val orbitalPeriodDays: Double?,
        val planetJupiterMass: Double?,
        val planetJupiterRadius: Double?,
        val planetDensity: Double?
) : Mappable<ConfirmedPlanetLocal> {

    override fun mapToResult(): Result<ConfirmedPlanetLocal> {
        val localConfirmedPlanet = ConfirmedPlanetLocal(
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
        return Result.Success(localConfirmedPlanet)
    }

    fun getPlanetImage() = PlanetImage.from(this)
}