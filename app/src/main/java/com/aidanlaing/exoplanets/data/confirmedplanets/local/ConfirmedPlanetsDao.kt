package com.aidanlaing.exoplanets.data.confirmedplanets.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ConfirmedPlanetsDao {

    @Query("SELECT * FROM confirmed_planet")
    fun get(): List<ConfirmedPlanetLocal>

    @Query("SELECT * FROM confirmed_planet WHERE planetName LIKE :planetName LIMIT 1")
    fun get(planetName: String): ConfirmedPlanetLocal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(confirmedPlanets: List<ConfirmedPlanetLocal>)

}