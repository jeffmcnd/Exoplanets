package com.aidanlaing.exoplanets.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsDataSource
import com.aidanlaing.exoplanets.screens.confirmedplanetdetail.ConfirmedPlanetDetailViewModel
import com.aidanlaing.exoplanets.screens.main.MainViewModel
import kotlin.coroutines.experimental.CoroutineContext

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
        private val uiContext: CoroutineContext,
        private val ioContext: CoroutineContext,
        private val confirmedPlanetsDataSource: ConfirmedPlanetsDataSource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: ViewModel? = when {

            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(uiContext, ioContext, confirmedPlanetsDataSource)

            modelClass.isAssignableFrom(ConfirmedPlanetDetailViewModel::class.java) ->
                ConfirmedPlanetDetailViewModel(uiContext, ioContext, confirmedPlanetsDataSource)

            else -> null

        }

        return viewModel as T
    }

}