package com.aidanlaing.exoplanets.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetLocal
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsDao

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