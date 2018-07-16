package com.aidanlaing.exoplanets.data.confirmedplanets

import androidx.room.Entity
import androidx.room.PrimaryKey

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
) {

    companion object {
        fun from(confirmedPlanet: ConfirmedPlanet) = ConfirmedPlanetLocal(
                confirmedPlanet.planetName,
                confirmedPlanet.hostStarName,
                confirmedPlanet.planetLetter,
                confirmedPlanet.discoveryMethod,
                confirmedPlanet.numPlanetsInSystem,
                confirmedPlanet.orbitalPeriodDays,
                confirmedPlanet.planetJupiterMass,
                confirmedPlanet.planetJupiterRadius,
                confirmedPlanet.planetDensity
        )
    }

}