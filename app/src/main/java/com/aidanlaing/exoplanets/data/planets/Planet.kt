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
        val discoveryMethod: String?,
        val jupiterRadius: Double?,
        val jupiterMass: Double?,
        val density: Double?,
        val orbitalPeriodDays: Double?,
        val starName: String?,
        val starDistanceParsecs: Double?,
        val starTemperatureKelvin: Double?,
        val starSunRadius: Double?,
        val starSunMass: Double?,
        val numPlanetsInSystem: Int?,
        var isFavourite: Boolean
) : Mappable<PlanetLocal>, Parcelable {

    override fun mapToResult(): Result<PlanetLocal> {
        val localPlanet = PlanetLocal(
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
                isFavourite
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

    fun getRoundedDistanceParsecsOrBlank() = starDistanceParsecs?.roundToInt()?.toString() ?: ""
    fun getDistanceParsecsOrBlank() = starDistanceParsecs?.toString() ?: ""

    fun getDiscoveryMethodOrBlank() = discoveryMethod ?: ""

    fun getJupiterRadiusOrBlank() = jupiterRadius?.toString() ?: ""

    fun getJupiterMassOrBlank() = jupiterMass?.toString() ?: ""

    fun getDensityOrBlank() = density?.toString() ?: ""

    fun getOrbitalPeriodDaysOrBlank() = orbitalPeriodDays?.toString() ?: ""

    fun getStarNameOrBlank() = starName ?: ""

    fun getStarTempKelvinOrBlank() = starTemperatureKelvin?.toString() ?: ""

    fun getStarSunRadiusOrBlank() = starSunRadius?.toString() ?: ""

    fun getStarSunMassOrBlank() = starSunMass?.toString() ?: ""

    fun getNumPlanetsInSystemOrBlank() = numPlanetsInSystem?.toString() ?: ""
}