package com.aidanlaing.exoplanets.common

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.aidanlaing.exoplanets.data.planets.PlanetsDataSource
import com.aidanlaing.exoplanets.screens.favourites.FavouritesViewModel
import com.aidanlaing.exoplanets.screens.planetdetail.PlanetDetailViewModel
import com.aidanlaing.exoplanets.screens.planets.PlanetsViewModel
import com.aidanlaing.exoplanets.screens.search.SearchViewModel
import kotlin.coroutines.experimental.CoroutineContext

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
        private val uiContext: CoroutineContext,
        private val ioContext: CoroutineContext,
        private val planetsDataSource: PlanetsDataSource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: ViewModel? = when {

            modelClass.isAssignableFrom(PlanetsViewModel::class.java) ->
                PlanetsViewModel(uiContext, ioContext, planetsDataSource)

            modelClass.isAssignableFrom(PlanetDetailViewModel::class.java) ->
                PlanetDetailViewModel(uiContext, ioContext, planetsDataSource)

            modelClass.isAssignableFrom(FavouritesViewModel::class.java) ->
                FavouritesViewModel(uiContext, ioContext, planetsDataSource)

            modelClass.isAssignableFrom(SearchViewModel::class.java) ->
                FavouritesViewModel(uiContext, ioContext, planetsDataSource)

            else -> null

        }

        return viewModel as T
    }

}