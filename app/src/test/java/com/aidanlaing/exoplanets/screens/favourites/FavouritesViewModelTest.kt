package com.aidanlaing.exoplanets.screens.favourites

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.aidanlaing.exoplanets.InstantCoroutineContext
import com.aidanlaing.exoplanets.common.adapters.planets.PlanetClick
import com.aidanlaing.exoplanets.data.Result
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.data.planets.PlanetsRepo
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavouritesViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FavouritesViewModel

    private lateinit var planetsRepo: PlanetsRepo

    @Before
    fun setUp() {
        planetsRepo = mock()

        viewModel = FavouritesViewModel(
                InstantCoroutineContext(),
                InstantCoroutineContext(),
                planetsRepo
        )
    }

    @After
    fun tearDown() {
        PlanetsRepo.destroyInstance()
    }

    @Test
    fun onBackEvent() {
        viewModel.backClicked()

        assertNotNull(viewModel.onBackEvent().value)
    }

    @Test
    fun getFavouritePlanetsSuccess() {
        val planetA = Planet("a", "2017")
        val planetB = Planet("b", "2012")
        val favouritePlanets = arrayListOf(planetA, planetB)
        val favouritePlanetsResult = Result.Success(favouritePlanets)

        runBlocking {
            whenever(planetsRepo.getFavouritePlanets()).thenReturn(favouritePlanetsResult)
        }

        val expected = favouritePlanets.reversed()
        val actual = viewModel.getFavouritePlanets().value
        assertEquals(expected, actual)
    }

    @Test
    fun getFavouritePlanetsFailureGeneralError() {
        val error = Exception()
        val favouritePlanetsResult = Result.Failure(error)

        runBlocking {
            whenever(planetsRepo.getFavouritePlanets()).thenReturn(favouritePlanetsResult)
        }

        viewModel.getFavouritePlanets()

        val expected = true
        val actual = viewModel.showGeneralError().value
        assertEquals(expected, actual)
    }

    @Test
    fun goToDetailEvent() {
        val planetClick: PlanetClick = mock()

        viewModel.planetClicked(planetClick)

        val actual = viewModel.goToDetailEvent().value?.data
        assertEquals(planetClick, actual)
    }

    @Test
    fun showLoading() {
        val favouritePlanets = ArrayList<Planet>()
        val favouritePlanetsResult = Result.Success(favouritePlanets)

        runBlocking {
            whenever(planetsRepo.getFavouritePlanets()).thenReturn(favouritePlanetsResult)
        }

        val observer: Observer<Boolean> = mock()
        viewModel.showLoading().observeForever(observer)
        viewModel.getFavouritePlanets()

        val inOrderObserver = inOrder(observer)
        inOrderObserver.verify(observer).onChanged(true)
        inOrderObserver.verify(observer).onChanged(false)
    }

}