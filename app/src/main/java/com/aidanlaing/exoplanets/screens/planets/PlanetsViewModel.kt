package com.aidanlaing.exoplanets.screens.planets

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.data.planets.PlanetsDataSource
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import java.io.IOException
import kotlin.coroutines.experimental.CoroutineContext

class PlanetsViewModel(
        private val uiContext: CoroutineContext,
        private val ioContext: CoroutineContext,
        private val planetsDataSource: PlanetsDataSource
) : ViewModel() {

    private val planets = MutableLiveData<ArrayList<Planet>>()
    private val loading = MutableLiveData<Boolean>()
    private val noConnection = MutableLiveData<Boolean>()
    private val generalError = MutableLiveData<Boolean>()

    fun getPlanets(): LiveData<ArrayList<Planet>> {
        if (planets.value == null) loadPlanets()
        return planets
    }

    fun showLoading(): LiveData<Boolean> {
        if (loading.value == null) loading.value = false
        return loading
    }

    fun showNoConnection(): LiveData<Boolean> {
        if (noConnection.value == null) noConnection.value = false
        return noConnection
    }

    fun showGeneralError(): LiveData<Boolean> {
        if (generalError.value == null) generalError.value = false
        return generalError
    }

    fun retryClicked() {
        loadPlanets()
    }

    private fun loadPlanets() = launch(uiContext) {
        loading.value = true
        noConnection.value = false
        generalError.value = false

        val result = withContext(ioContext) {
            planetsDataSource.getPlanets()
        }

        when (result) {
            is Result.Success -> planets.value = result.data
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