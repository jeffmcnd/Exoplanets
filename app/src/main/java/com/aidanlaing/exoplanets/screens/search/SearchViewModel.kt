package com.aidanlaing.exoplanets.screens.search

import android.arch.lifecycle.ViewModel
import com.aidanlaing.exoplanets.data.planets.PlanetsDataSource
import kotlin.coroutines.experimental.CoroutineContext

class SearchViewModel(
        private val uiContext: CoroutineContext,
        private val ioContext: CoroutineContext,
        private val planetsDataSource: PlanetsDataSource
) : ViewModel() {

}