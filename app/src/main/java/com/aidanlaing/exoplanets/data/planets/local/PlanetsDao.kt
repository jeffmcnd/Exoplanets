package com.aidanlaing.exoplanets.data.planets.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface PlanetsDao {

    @Query("SELECT * FROM planet")
    fun get(): List<PlanetLocal>

    @Query("SELECT * FROM planet WHERE name LIKE :name LIMIT 1")
    fun get(name: String): PlanetLocal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(planets: List<PlanetLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(planet: PlanetLocal)

    @Query("SELECT * FROM planet WHERE isFavourite")
    fun getFavourites(): List<PlanetLocal>

}