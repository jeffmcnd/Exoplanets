package com.aidanlaing.exoplanets.screens.favourites

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.aidanlaing.exoplanets.common.adapters.planets.PlanetClick
import com.aidanlaing.exoplanets.common.livedata.SingleEvent
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.data.planets.PlanetsDataSource
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext

class FavouritesViewModel(
        private val uiContext: CoroutineContext,
        private val ioContext: CoroutineContext,
        private val planetsDataSource: PlanetsDataSource
) : ViewModel() {

    private val backEvent = MutableLiveData<SingleEvent<Nothing>>()
    private val planets = MutableLiveData<ArrayList<Planet>>()
    private val showLoading = MutableLiveData<Boolean>()
    private val showGeneralError = MutableLiveData<Boolean>()
    private val goToDetailEvent = MutableLiveData<SingleEvent<PlanetClick>>()

    fun onBackEvent(): LiveData<SingleEvent<Nothing>> = backEvent

    fun backClicked() {
        backEvent.value = SingleEvent()
    }

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

    fun showGeneralError(): LiveData<Boolean> {
        if (showGeneralError.value == null) showGeneralError.value = false
        return showGeneralError
    }

    fun retryClicked() {
        loadPlanets()
    }

    private fun loadPlanets() = launch(uiContext) {
        showLoading.value = true
        showGeneralError.value = false

        val result = withContext(ioContext) {
            planetsDataSource.getFavouritePlanets()
        }

        when (result) {
            is Result.Success -> planets.value = result.data.reversed().let { ArrayList(it) }
            is Result.Failure -> showGeneralError.value = true
        }

        showLoading.value = false
    }

}