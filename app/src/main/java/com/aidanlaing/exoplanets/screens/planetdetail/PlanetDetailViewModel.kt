package com.aidanlaing.exoplanets.screens.planetdetail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.aidanlaing.exoplanets.common.livedata.SingleEvent
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.data.planets.PlanetsDataSource
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext

class PlanetDetailViewModel(
        private val uiContext: CoroutineContext,
        private val ioContext: CoroutineContext,
        private val planetsDataSource: PlanetsDataSource
) : ViewModel() {

    private val isFavourite = MutableLiveData<Boolean>()
    private val backEvent = MutableLiveData<SingleEvent<Nothing>>()

    fun onBackEvent(): LiveData<SingleEvent<Nothing>> = backEvent

    fun backClicked() {
        backEvent.value = SingleEvent()
    }

    fun isFavourite(planet: Planet): LiveData<Boolean> {
        if (isFavourite.value == null) loadIsFavourite(planet)
        return isFavourite
    }

    fun favouriteClicked(planet: Planet) {
        val isFavourite = isFavourite.value
        if (isFavourite != null) {
            planet.isFavourite = !isFavourite
            savePlanet(planet)
        }
    }

    private fun savePlanet(planet: Planet) = launch(uiContext) {
        val favouritePlanetResult = withContext(ioContext) {
            planetsDataSource.savePlanet(planet)
        }

        when (favouritePlanetResult) {
            is Result.Success -> isFavourite.value = planet.isFavourite
            is Result.Failure -> isFavourite.value = null
        }
    }

    private fun loadIsFavourite(planet: Planet) = launch(uiContext) {
        val isFavouriteResult = withContext(ioContext) {
            planetsDataSource.isFavouritePlanet(planet.name)
        }

        when (isFavouriteResult) {
            is Result.Success -> isFavourite.value = isFavouriteResult.data
            is Result.Failure -> isFavourite.value = null
        }
    }

}