package com.aidanlaing.exoplanets.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.aidanlaing.exoplanets.data.confirmedplanets.local.ConfirmedPlanetLocal
import com.aidanlaing.exoplanets.data.confirmedplanets.local.ConfirmedPlanetsDao

@Database(entities = [ConfirmedPlanetLocal::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun confirmedPlanetsDao(): ConfirmedPlanetsDao

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