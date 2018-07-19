package com.aidanlaing.exoplanets.data.planets.remote

import com.aidanlaing.exoplanets.common.exceptions.MappingException
import com.aidanlaing.exoplanets.data.Mappable
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.Planet
import com.google.gson.annotations.SerializedName

data class PlanetRemote(
        @SerializedName("pl_name") val planetName: String?,
        @SerializedName("pl_hostname") val hostStarName: String?,
        @SerializedName("pl_letter") val planetLetter: String?,
        @SerializedName("pl_discmethod") val discoveryMethod: String?,
        @SerializedName("pl_pnum") val numPlanetsInSystem: Int?,
        @SerializedName("pl_orbper") val orbitalPeriodDays: Double?,
        @SerializedName("pl_bmassj") val planetJupiterMass: Double?,
        @SerializedName("pl_radj") val planetJupiterRadius: Double?,
        @SerializedName("pl_dens") val planetDensity: Double?
) : Mappable<Planet> {

    override fun mapToResult(): Result<Planet> {
        val planetName = planetName
        val hostStarName = hostStarName

        if (planetName == null || planetName.isBlank() || hostStarName == null) {
            return Result.Failure(MappingException())
        }

        val planet = Planet(
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

        return Result.Success(planet)
    }
}