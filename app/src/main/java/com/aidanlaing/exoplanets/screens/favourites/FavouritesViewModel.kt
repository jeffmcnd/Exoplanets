package com.aidanlaing.exoplanets.screens.favourites

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.aidanlaing.exoplanets.common.adapters.planets.PlanetClick
import com.aidanlaing.exoplanets.common.livedata.SingleDataEvent
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

    private val backEvent = MutableLiveData<SingleEvent>()
    private val favouritePlanets = MutableLiveData<ArrayList<Planet>>()
    private val goToDetailEvent = MutableLiveData<SingleDataEvent<PlanetClick>>()
    private val showLoading = MutableLiveData<Boolean>()
    private val showGeneralError = MutableLiveData<Boolean>()

    fun onBackEvent(): LiveData<SingleEvent> = backEvent
    fun backClicked() {
        backEvent.value = SingleEvent()
    }

    fun getFavouritePlanets(): LiveData<ArrayList<Planet>> {
        if (favouritePlanets.value == null) loadFavouritePlanets()
        return favouritePlanets
    }

    fun goToDetailEvent(): LiveData<SingleDataEvent<PlanetClick>> = goToDetailEvent
    fun planetClicked(planetClick: PlanetClick) {
        goToDetailEvent.value = SingleDataEvent(planetClick)
    }

    fun showLoading(): LiveData<Boolean> {
        if (showLoading.value == null) showLoading.value = false
        return showLoading
    }

    fun showGeneralError(): LiveData<Boolean> {
        if (showGeneralError.value == null) showGeneralError.value = false
        return showGeneralError
    }

    fun retryClicked() {
        loadFavouritePlanets()
    }

    private fun loadFavouritePlanets() = launch(uiContext) {
        showLoading.value = true
        showGeneralError.value = false

        val getFavouritePlanetsResult = withContext(ioContext) {
            planetsDataSource.getFavouritePlanets()
        }

        when (getFavouritePlanetsResult) {
            is Result.Success -> favouritePlanets.value = getFavouritePlanetsResult
                    .data.reversed()
                    .let { ArrayList(it) }

            is Result.Failure -> showGeneralError.value = true
        }

        showLoading.value = false
    }

}