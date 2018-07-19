package com.aidanlaing.exoplanets.screens.confirmedplanetdetail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanet
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsDataSource
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import java.io.IOException
import kotlin.coroutines.experimental.CoroutineContext

class ConfirmedPlanetDetailViewModel(
        private val uiContext: CoroutineContext,
        private val ioContext: CoroutineContext,
        private val confirmedPlanetsDataSource: ConfirmedPlanetsDataSource
) : ViewModel() {

    private val confirmedPlanet = MutableLiveData<ConfirmedPlanet>()
    private val loading = MutableLiveData<Boolean>()
    private val noConnection = MutableLiveData<Boolean>()
    private val generalError = MutableLiveData<Boolean>()

    init {
        loading.value = false
        noConnection.value = false
        generalError.value = false
    }

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

    private fun loadConfirmedPlanet(planetName: String) = launch(uiContext) {
        loading.value = true
        noConnection.value = false
        generalError.value = false

        val result = withContext(ioContext) {
            confirmedPlanetsDataSource.getConfirmedPlanet(planetName)
        }

        when (result) {
            is Result.Success -> confirmedPlanet.value = result.data
            is Result.Failure -> onError(result.error)
        }

        loading.value = false
    }

    private fun onError(exception: Exception) {
        when (exception) {
            is IOException -> noConnection.value = true
            else -> generalError.value = true
        }
    }
}