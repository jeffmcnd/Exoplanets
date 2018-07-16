package com.aidanlaing.exoplanets.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val error = MutableLiveData<Exception>()
    private val loading = MutableLiveData<Boolean>()

    fun getConfirmedPlanets(refresh: Boolean): LiveData<ArrayList<ConfirmedPlanet>> {
        loadConfirmedPlanets(refresh)
        return confirmedPlanets
    }

    fun getError(): LiveData<Exception> = error

    fun getLoading(): LiveData<Boolean> = loading

    private fun loadConfirmedPlanets(refresh: Boolean) = launch(UI) {
        try {
            loading.value = true
            confirmedPlanets.value = withContext(CommonPool) {
                confirmedPlanetsDataSource.getConfirmedPlanets(refresh)
            }
            loading.value = false
        } catch (exception: Exception) {
            error.value = exception
        }
    }

}