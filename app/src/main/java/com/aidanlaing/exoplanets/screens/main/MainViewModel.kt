package com.aidanlaing.exoplanets.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanet
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsDataSource
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import java.io.IOException
import kotlin.coroutines.experimental.CoroutineContext

class MainViewModel(
        private val uiContext: CoroutineContext,
        private val ioContext: CoroutineContext,
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

    private fun loadConfirmedPlanets() = launch(uiContext) {
        loading.value = true
        noConnection.value = false
        generalError.value = false

        val result = withContext(ioContext) {
            confirmedPlanetsDataSource.getConfirmedPlanets()
        }

        when (result) {
            is Result.Success -> confirmedPlanets.value = result.data
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