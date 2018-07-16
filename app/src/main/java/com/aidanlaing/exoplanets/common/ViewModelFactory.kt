package com.aidanlaing.exoplanets.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsDataSource
import com.aidanlaing.exoplanets.screens.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
        private val confirmedPlanetsDataSource: ConfirmedPlanetsDataSource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: ViewModel? = when {

            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(confirmedPlanetsDataSource)

            else -> null

        }

        return viewModel as T
    }

}