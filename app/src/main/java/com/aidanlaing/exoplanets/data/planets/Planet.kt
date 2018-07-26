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
        val discoveryMethod: String? = null,
        val jupiterRadius: Double? = null,
        val jupiterMass: Double? = null,
        val density: Double? = null,
        val orbitalPeriodDays: Double? = null,
        val starName: String? = null,
        val starDistanceParsecs: Double? = null,
        val starTemperatureKelvin: Double? = null,
        val starSunRadius: Double? = null,
        val starSunMass: Double? = null,
        val numPlanetsInSystem: Int? = null,
        var isFavourite: Boolean = false
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