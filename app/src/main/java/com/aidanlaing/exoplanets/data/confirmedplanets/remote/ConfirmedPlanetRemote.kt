package com.aidanlaing.exoplanets.data.confirmedplanets.remote

import com.aidanlaing.exoplanets.common.exceptions.MappingException
import com.aidanlaing.exoplanets.data.Mappable
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanet
import com.google.gson.annotations.SerializedName

data class ConfirmedPlanetRemote(
        @SerializedName("pl_name") val planetName: String?,
        @SerializedName("pl_hostname") val hostStarName: String?,
        @SerializedName("pl_letter") val planetLetter: String?,
        @SerializedName("pl_discmethod") val discoveryMethod: String?,
        @SerializedName("pl_pnum") val numPlanetsInSystem: Int?,
        @SerializedName("pl_orbper") val orbitalPeriodDays: Double?,
        @SerializedName("pl_bmassj") val planetJupiterMass: Double?,
        @SerializedName("pl_radj") val planetJupiterRadius: Double?,
        @SerializedName("pl_dens") val planetDensity: Double?
) : Mappable<ConfirmedPlanet> {

    override fun mapToResult(): Result<ConfirmedPlanet> {
        val planetName = planetName
        val hostStarName = hostStarName

        if (planetName == null || planetName.isBlank() || hostStarName == null) {
            return Result.Failure(MappingException())
        }

        val confirmedPlanet = ConfirmedPlanet(
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

        return Result.Success(confirmedPlanet)
    }
}