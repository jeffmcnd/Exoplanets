package com.aidanlaing.exoplanets.data.planets.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.aidanlaing.exoplanets.data.Mappable
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.Planet

@Entity(tableName = "planet")
data class PlanetLocal(
        @PrimaryKey
        val planetName: String,
        val hostStarName: String?,
        val planetLetter: String?,
        val discoveryMethod: String?,
        val numPlanetsInSystem: Int?,
        val orbitalPeriodDays: Double?,
        val planetJupiterMass: Double?,
        val planetJupiterRadius: Double?,
        val planetDensity: Double?
) : Mappable<Planet> {

    override fun mapToResult(): Result<Planet> {
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