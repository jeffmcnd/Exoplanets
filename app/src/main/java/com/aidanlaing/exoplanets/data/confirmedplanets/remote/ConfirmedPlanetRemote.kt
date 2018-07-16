package com.aidanlaing.exoplanets.data.confirmedplanets.remote

import com.google.gson.annotations.SerializedName

data class ConfirmedPlanetRemote(
        @SerializedName("pl_name") val planetName: String?,
        @SerializedName("pl_hostname") val hostStarName: String?,
        @SerializedName("pl_letter") val planetLetter: String?,
        @SerializedName("pl_discmethod") val discoveryMethod: String?,
        @SerializedName("pl_pnum") val numPlanetsInSystem: Int?,
        @SerializedName("pl_orbper") val orbitalPeriodDays: Double?,
        @SerializedName("pl_bmassj") val planetJupiterMass: Double?,
        @SerializedName("pl_radj") val planetJupiterRadius: Double?,
        @SerializedName("pl_dens") val planetDensity: Double?
)