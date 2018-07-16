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
import java.io.IOException

class MainViewModel(
        private val confirmedPlanetsDataSource: ConfirmedPlanetsDataSource
) : ViewModel() {

    private val confirmedPlanets = MutableLiveData<ArrayList<ConfirmedPlanet>>()
    private val loading = MutableLiveData<Boolean>()
    private val noConnection = MutableLiveData<Boolean>()
    private val generalError = MutableLiveData<Boolean>()

    fun getConfirmedPlanets(): LiveData<ArrayList<ConfirmedPlanet>> {
        if (confirmedPlanets.value == null) {
            loadConfirmedPlanets()
        }
        return confirmedPlanets
    }

    fun showLoading(): LiveData<Boolean> = loading
    fun showNoConnection(): LiveData<Boolean> = noConnection
    fun showGeneralError(): LiveData<Boolean> = generalError

    fun retryClicked() {
        loadConfirmedPlanets()
    }

    private fun loadConfirmedPlanets() = launch(UI) {
        loading.value = true
        noConnection.value = false
        generalError.value = false

        try {
            confirmedPlanets.value = withContext(CommonPool) {
                confirmedPlanetsDataSource.getConfirmedPlanets()
            }
        } catch (exception: IOException) {
            noConnection.value = true
        } catch (exception: Exception) {
            generalError.value = true
        }

        loading.value = false
    }

}