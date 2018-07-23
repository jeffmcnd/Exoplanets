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

    fun getPlanetImage() = PlanetImage.from(this)

    fun getRoundedDistanceParsecs() = starDistanceParsecs?.roundToInt()?.toString() ?: ""
    fun getDistanceParsecs() = starDistanceParsecs?.toString() ?: ""
}