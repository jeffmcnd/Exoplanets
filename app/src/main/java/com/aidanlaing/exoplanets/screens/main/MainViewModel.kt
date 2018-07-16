package com.aidanlaing.exoplanets.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanet
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsDataSource
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class MainViewModel(
        private val confirmedPlanetsDataSource: ConfirmedPlanetsDataSource
) : ViewModel() {

    private val confirmedPlanets = MutableLiveData<ArrayList<ConfirmedPlanet>>()

    fun getConfirmedPlanets(): LiveData<ArrayList<ConfirmedPlanet>> {
        loadConfirmedPlanets()
        return confirmedPlanets
    }

    fun getConfirmedPlanetsSize(): LiveData<String> = Transformations
            .map(confirmedPlanets) { confirmedPlanets ->
                confirmedPlanets.size.toString()
            }

    private fun loadConfirmedPlanets() = launch(UI) {
        try {
            val result = withContext(CommonPool) {
                confirmedPlanetsDataSource.getConfirmedPlanets()
            }
            confirmedPlanets.value = result
        } catch (exception: Exception) {
            println(exception.message)
        }
    }

}