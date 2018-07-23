package com.aidanlaing.exoplanets.data.planets.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.aidanlaing.exoplanets.data.Mappable
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.Planet

@Entity(tableName = "planet")
data class PlanetLocal(
        @PrimaryKey
        val name: String,
        val isFavourite: Boolean,
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
) : Mappable<Planet> {

    override fun mapToResult(): Result<Planet> {
        val planet = Planet(
                name,
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

        return Result.Success(planet)
    }

}