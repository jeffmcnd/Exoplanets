package com.aidanlaing.exoplanets.data.confirmedplanets

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ConfirmedPlanetsDao {

    @Query("SELECT * FROM confirmed_planet")
    fun get(): List<ConfirmedPlanetLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(confirmedPlanets: List<ConfirmedPlanetLocal>)

}