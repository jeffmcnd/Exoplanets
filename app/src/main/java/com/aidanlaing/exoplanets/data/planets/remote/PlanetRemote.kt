package com.aidanlaing.exoplanets.data.planets.remote

import com.aidanlaing.exoplanets.common.exceptions.MappingException
import com.aidanlaing.exoplanets.data.Mappable
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.Planet
import com.google.gson.annotations.SerializedName

data class PlanetRemote(
        @SerializedName("pl_name") val name: String?,
        @SerializedName("pl_disc") val discoveryYear: String?,
        @SerializedName("pl_discmethod") val discoveryMethod: String?,
        @SerializedName("pl_radj") val jupiterRadius: Double?,
        @SerializedName("pl_bmassj") val jupiterMass: Double?,
        @SerializedName("pl_dens") val density: Double?,
        @SerializedName("pl_orbper") val orbitalPeriodDays: Double?,
        @SerializedName("pl_hostname") val starName: String?,
        @SerializedName("st_dist") val starDistanceParsecs: Double?,
        @SerializedName("st_teff") val starTemperatureKelvin: Double?,
        @SerializedName("st_rad") val starSunRadius: Double?,
        @SerializedName("st_mass") val starSunMass: Double?,
        @SerializedName("pl_pnum") val numPlanetsInSystem: Int?
) : Mappable<Planet> {

    override fun mapToResult(): Result<Planet> {
        val name = name
        val discoveryYear = discoveryYear

        if (name == null || name.isBlank() || discoveryYear == null || discoveryYear.isBlank()) {
            return Result.Failure(MappingException())
        }

        val planet = Planet(
                name,
                discoveryYear,
                discoveryMethod,
                jupiterRadius,
                jupiterMass,
                density,
                orbitalPeriodDays,
                starName,
                starDistanceParsecs,
                starTemperatureKelvin,
                starSunRadius,
                starSunMass,
                numPlanetsInSystem,
                false
        )

        return Result.Success(planet)
    }
}