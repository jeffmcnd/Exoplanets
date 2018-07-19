package com.aidanlaing.exoplanets.data.confirmedplanets.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.aidanlaing.exoplanets.data.Mappable
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanet

@Entity(tableName = "confirmed_planet")
data class ConfirmedPlanetLocal(
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
) : Mappable<ConfirmedPlanet> {

    override fun mapToResult(): Result<ConfirmedPlanet> {
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