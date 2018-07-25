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
) : Mappable<Planet> {

    override fun mapToResult(): Result<Planet> {
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
                isFavourite
        )

        return Result.Success(planet)
    }

}