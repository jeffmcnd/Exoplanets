package com.aidanlaing.exoplanets.screens.confirmedplanetdetail

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

class ConfirmedPlanetDetailViewModel(
        private val confirmedPlanetsDataSource: ConfirmedPlanetsDataSource
): ViewModel() {

    private val confirmedPlanet = MutableLiveData<ConfirmedPlanet>()
    private val loading = MutableLiveData<Boolean>()
    private val noConnection = MutableLiveData<Boolean>()
    private val generalError = MutableLiveData<Boolean>()

    fun getConfirmedPlanet(planetName: String): LiveData<ConfirmedPlanet> {
        if (confirmedPlanet.value == null) {
            loadConfirmedPlanet(planetName)
        }
        return confirmedPlanet
    }

    fun showLoading(): LiveData<Boolean> = loading
    fun showNoConnection(): LiveData<Boolean> = noConnection
    fun showGeneralError(): LiveData<Boolean> = generalError

    fun retryClicked(planetName: String) {
        loadConfirmedPlanet(planetName)
    }

    private fun loadConfirmedPlanet(planetName: String) = launch(UI) {
        loading.value = true
        noConnection.value = false
        generalError.value = false

        try {
            confirmedPlanet.value = withContext(CommonPool) {
                confirmedPlanetsDataSource.getConfirmedPlanet(planetName)
            }
        } catch (exception: IOException) {
            noConnection.value = true
        } catch (exception: Exception) {
            generalError.value = true
        }

        loading.value = false
    }
}