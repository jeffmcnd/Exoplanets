package com.aidanlaing.exoplanets.data.planets.remote

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface PlanetsApi {

    @GET("/cgi-bin/nstedAPI/nph-nstedAPI")
    fun get(
            @Query("table") table: String = "exoplanets",
            @Query("select") select: String = "pl_name,pl_hostname,pl_letter,pl_discmethod,pl_pnum,pl_orbper,pl_bmassj,pl_radj,pl_dens,st_dist,st_teff,st_mass,st_rad",
            @Query("order") order: String = "dec",
            @Query("format") format: String = "json",
            @Query("where") where: String? = null
    ): Deferred<ArrayList<PlanetRemote>>

}