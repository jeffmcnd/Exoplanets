package com.aidanlaing.exoplanets.screens.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.aidanlaing.exoplanets.common.adapters.planets.PlanetClick
import com.aidanlaing.exoplanets.common.events.SingleDataEvent
import com.aidanlaing.exoplanets.common.events.SingleEvent
import com.aidanlaing.exoplanets.common.exceptions.NoConnectionException
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.data.planets.PlanetsDataSource
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext

class SearchViewModel(
        private val uiContext: CoroutineContext,
        private val ioContext: CoroutineContext,
        private val planetsDataSource: PlanetsDataSource
) : ViewModel() {

    private val backEvent = MutableLiveData<SingleEvent>()
    private val planets = MutableLiveData<ArrayList<Planet>>()
    private val goToDetailEvent = MutableLiveData<SingleDataEvent<PlanetClick>>()
    private val showLoading = MutableLiveData<Boolean>()
    private val showNoConnection = MutableLiveData<Boolean>()
    private val showGeneralError = MutableLiveData<Boolean>()

    fun onBackEvent() = backEvent
    fun backClicked() {
        backEvent.value = SingleEvent()
    }

    fun getPlanets(): LiveData<ArrayList<Planet>> = planets

    fun goToDetailEvent(): LiveData<SingleDataEvent<PlanetClick>> = goToDetailEvent
    fun planetClicked(planetClick: PlanetClick) {
        goToDetailEvent.value = SingleDataEvent(planetClick)
    }

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

    fun retryClicked(searchText: String) {
        loadPlanets(searchText)
    }

    fun searchClicked(searchText: String) {
        loadPlanets(searchText)
    }

    private fun loadPlanets(searchText: String) = launch(uiContext) {
        showLoading.value = true
        showNoConnection.value = false
        showGeneralError.value = false

        val getPlanetsResult = withContext(ioContext) {
            planetsDataSource.getPlanets()
        }

        when (getPlanetsResult) {
            is Result.Success -> planets.value = withContext(ioContext) {
                getPlanetsResult.data
                        .filter { planet ->
                            searchText.isNotBlank() && planet.name.contains(searchText, true)
                        }
                        .sortedWith(Comparator { planetOne, planetTwo ->
                            planetOne.compareTo(planetTwo)
                        })
                        .mapTo(ArrayList()) { it }
            }

            is Result.Failure -> onError(getPlanetsResult.error)
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