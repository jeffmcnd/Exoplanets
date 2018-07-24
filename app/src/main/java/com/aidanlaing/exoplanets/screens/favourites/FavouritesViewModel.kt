package com.aidanlaing.exoplanets.screens.favourites

import android.arch.lifecycle.ViewModel
import com.aidanlaing.exoplanets.data.planets.PlanetsDataSource
import kotlin.coroutines.experimental.CoroutineContext

class FavouritesViewModel(
        private val uiContext: CoroutineContext,
        private val ioContext: CoroutineContext,
        private val planetsDataSource: PlanetsDataSource
) : ViewModel() {

}