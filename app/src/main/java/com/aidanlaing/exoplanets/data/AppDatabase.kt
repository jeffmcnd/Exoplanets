package com.aidanlaing.exoplanets.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.aidanlaing.exoplanets.data.planets.local.PlanetLocal
import com.aidanlaing.exoplanets.data.planets.local.PlanetsDao

@Database(entities = [PlanetLocal::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun planetsDao(): PlanetsDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, name: String) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context, name)
                    .also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context, name: String) = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                name
        ).fallbackToDestructiveMigration()
                .build()
    }

}