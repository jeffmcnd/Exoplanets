package com.aidanlaing.exoplanets.data.confirmedplanets

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
            val planetName = confirmedPlanetRemote.planetName ?: return null

            return ConfirmedPlanet(
                    planetName,
                    confirmedPlanetRemote.hostStarName,
                    confirmedPlanetRemote.planetLetter,
                    confirmedPlanetRemote.discoveryMethod,
                    confirmedPlanetRemote.numPlanetsInSystem,
                    confirmedPlanetRemote.orbitalPeriodDays,
                    confirmedPlanetRemote.planetJupiterMass,
                    confirmedPlanetRemote.planetJupiterRadius,
                    confirmedPlanetRemote.planetDensity
            )
        }

        fun from(confirmedPlanetLocal: ConfirmedPlanetLocal): ConfirmedPlanet {
            return ConfirmedPlanet(
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

}