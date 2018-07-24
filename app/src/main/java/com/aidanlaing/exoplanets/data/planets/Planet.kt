package com.aidanlaing.exoplanets.data.planets

import android.os.Parcelable
import com.aidanlaing.exoplanets.data.Mappable
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.local.PlanetLocal
import kotlinx.android.parcel.Parcelize
import kotlin.math.roundToInt

@Parcelize
data class Planet(
        val name: String,
        val discoveryYear: String,
        var isFavourite: Boolean,
        val letter: String?,
        val discoveryMethod: String?,
        val numPlanetsInSystem: Int?,
        val orbitalPeriodDays: Double?,
        val jupiterMass: Double?,
        val jupiterRadius: Double?,
        val density: Double?,
        val starName: String?,
        val starDistanceParsecs: Double?,
        val starTemperatureKelvin: Double?,
        val starSunMass: Double?,
        val starSunRadius: Double?
) : Mappable<PlanetLocal>, Parcelable {

    override fun mapToResult(): Result<PlanetLocal> {
        val localPlanet = PlanetLocal(
                name,
                discoveryYear,
                isFavourite,
                letter,
                discoveryMethod,
                numPlanetsInSystem,
                orbitalPeriodDays,
                jupiterMass,
                jupiterRadius,
                density,
                starName,
                starDistanceParsecs,
                starTemperatureKelvin,
                starSunMass,
                starSunRadius
        )

        return Result.Success(localPlanet)
    }

    fun compareTo(planet: Planet): Int {
        val discoverYearInt = discoveryYear.toIntOrNull()
        val compareDiscoveryYearInt = planet.discoveryYear.toIntOrNull()
        return when {
            discoverYearInt == null && compareDiscoveryYearInt == null -> 0
            discoverYearInt == null -> 1
            compareDiscoveryYearInt == null -> -1
            discoverYearInt > compareDiscoveryYearInt -> -1
            discoverYearInt < compareDiscoveryYearInt -> 1
            else -> name.compareTo(planet.name)
        }
    }

    fun getPlanetImage() = PlanetImage.from(this)

    fun getRoundedDistanceParsecs() = starDistanceParsecs?.roundToInt()?.toString() ?: ""
    fun getDistanceParsecs() = starDistanceParsecs?.toString() ?: ""
}