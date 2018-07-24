package com.aidanlaing.exoplanets.screens.planets

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.aidanlaing.exoplanets.common.adapters.planets.PlanetClick
import com.aidanlaing.exoplanets.common.exceptions.NoConnectionException
import com.aidanlaing.exoplanets.common.livedata.SingleEvent
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.data.planets.PlanetsDataSource
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext

class PlanetsViewModel(
        private val uiContext: CoroutineContext,
        private val ioContext: CoroutineContext,
        private val planetsDataSource: PlanetsDataSource
) : ViewModel() {

    private val planets = MutableLiveData<ArrayList<Planet>>()
    private val showLoading = MutableLiveData<Boolean>()
    private val showNoConnection = MutableLiveData<Boolean>()
    private val showGeneralError = MutableLiveData<Boolean>()
    private val goToDetailEvent = MutableLiveData<SingleEvent<PlanetClick>>()
    private val goToFavouritesEvent = MutableLiveData<SingleEvent<Nothing>>()
    private val goToSearchEvent = MutableLiveData<SingleEvent<Nothing>>()

    fun getPlanets(): LiveData<ArrayList<Planet>> {
        if (planets.value == null) loadPlanets()
        return planets
    }

    fun planetClicked(planetClick: PlanetClick) {
        goToDetailEvent.value = SingleEvent(planetClick)
    }

    fun goToDetailEvent(): LiveData<SingleEvent<PlanetClick>> = goToDetailEvent

    fun showLoading(): LiveData<Boolean> {
        if (showLoading.value == null) showLoading.value = false
        return showLoading
    }

    fun showNoConnection(): LiveData<Boolean> {
        if (showNoConnection.value == null) showNoConnection.value = false
        return showNoConnection
    }

    fun showGeneralError(): LiveData<Boolean> {
        if (showGeneralError.value == null) showGeneralError.value = false
        return showGeneralError
    }

    fun retryClicked() {
        loadPlanets()
    }

    fun favouritesClicked() {
        goToFavouritesEvent.value = SingleEvent()
    }

    fun goToFavouritesEvent(): LiveData<SingleEvent<Nothing>> = goToFavouritesEvent

    fun searchClicked() {
        goToSearchEvent.value = SingleEvent()
    }

    fun goToSearchEvent(): LiveData<SingleEvent<Nothing>> = goToSearchEvent

    private fun loadPlanets() = launch(uiContext) {
        showLoading.value = true
        showNoConnection.value = false
        showGeneralError.value = false

        val result = withContext(ioContext) {
            planetsDataSource.getPlanets()
        }

        when (result) {
            is Result.Success -> planets.value = result.data
                    .sortedWith(Comparator { planetOne, planetTwo ->
                        planetOne.compareTo(planetTwo)
                    })
                    .mapTo(ArrayList()) { it }

            is Result.Failure -> onError(result.error)
        }

        showLoading.value = false
    }

    private fun onError(exception: Exception) {
        when (exception) {
            is NoConnectionException -> showNoConnection.value = true
            else -> showGeneralError.value = true
        }
    }

}