package com.aidanlaing.exoplanets.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.aidanlaing.exoplanets.data.confirmedplanets.local.ConfirmedPlanetLocal
import com.aidanlaing.exoplanets.data.confirmedplanets.local.ConfirmedPlanetsDao

/**
 * Created by Aidan Laing on 2017-12-09.
 *
 */
@Database(entities = [ConfirmedPlanetLocal::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun confirmedPlanetsDao(): ConfirmedPlanetsDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context)
                    .also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app.db"
        ).fallbackToDestructiveMigration()
                .build()
    }

}