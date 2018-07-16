package com.aidanlaing.exoplanets.data.confirmedplanets

import com.aidanlaing.exoplanets.data.confirmedplanets.local.ConfirmedPlanetLocal
import com.aidanlaing.exoplanets.data.confirmedplanets.remote.ConfirmedPlanetRemote

data class ConfirmedPlanet(
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
        fun fromOrNull(confirmedPlanetRemote: ConfirmedPlanetRemote): ConfirmedPlanet? {
            val planetName = confirmedPlanetRemote.planetName
            val hostStarName = confirmedPlanetRemote.hostStarName

            if (planetName == null || planetName.isBlank() || hostStarName == null) return null

            return ConfirmedPlanet(
                    planetName,
                    hostStarName,
                    confirmedPlanetRemote.planetLetter,
                    confirmedPlanetRemote.discoveryMethod,
                    confirmedPlanetRemote.numPlanetsInSystem,
                    confirmedPlanetRemote.orbitalPeriodDays,
                    confirmedPlanetRemote.planetJupiterMass,
                    confirmedPlanetRemote.planetJupiterRadius,
                    confirmedPlanetRemote.planetDensity
            )
        }

        fun from(confirmedPlanetLocal: ConfirmedPlanetLocal) = ConfirmedPlanet(
                confirmedPlanetLocal.planetName,
                confirmedPlanetLocal.hostStarName,
                confirmedPlanetLocal.planetLetter,
                confirmedPlanetLocal.discoveryMethod,
                confirmedPlanetLocal.numPlanetsInSystem,
                confirmedPlanetLocal.orbitalPeriodDays,
                confirmedPlanetLocal.planetJupiterMass,
                confirmedPlanetLocal.planetJupiterRadius,
                confirmedPlanetLocal.planetDensity
        )
    }

}