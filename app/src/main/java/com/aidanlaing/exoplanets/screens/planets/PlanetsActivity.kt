package com.aidanlaing.exoplanets.screens.planets

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.Constants
import com.aidanlaing.exoplanets.common.Injector
import com.aidanlaing.exoplanets.common.adapters.planets.PlanetsAdapter
import com.aidanlaing.exoplanets.common.livedata.NonNullObserver
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.screens.favourites.FavouritesActivity
import com.aidanlaing.exoplanets.screens.planetdetail.PlanetDetailActivity
import com.aidanlaing.exoplanets.screens.search.SearchActivity
import kotlinx.android.synthetic.main.activity_planets.*

class PlanetsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planets)

        val viewModel = ViewModelProviders
                .of(this, Injector.provideViewModelFactory(this))
                .get(PlanetsViewModel::class.java)

        setUpPlanets(viewModel)
        setUpLoading(viewModel)
        setUpNoConnection(viewModel)
        setUpGeneralError(viewModel)
        setUpFavouritesListener(viewModel)
        setUpSearchListener(viewModel)
    }

    private fun setUpPlanets(viewModel: PlanetsViewModel) {
        val planetsAdapter = PlanetsAdapter({ planetClick ->
            layout.requestFocus()
            viewModel.planetClicked(planetClick)
        })

        val layoutManager = LinearLayoutManager(this)

        viewModel.goToDetailEvent().observe(this, NonNullObserver { event ->
            event.invokeIfNotHandled { data ->
                goToPlanetDetail(data.planet, data.planetImageView)
            }
        })

        viewModel.getPlanets()
                .observe(this, NonNullObserver { planets ->
                    planetsAdapter.replaceItems(planets)
                })

        planetsRv.layoutManager = layoutManager
        planetsRv.adapter = planetsAdapter
    }

    private fun setUpLoading(viewModel: PlanetsViewModel) {
        viewModel.showLoading().observe(this, NonNullObserver { show ->
            if (show) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.GONE
        })
    }

    private fun setUpNoConnection(viewModel: PlanetsViewModel) {
        viewModel.showNoConnection().observe(this, NonNullObserver { show ->
            val titleText = getString(R.string.no_internet_connection)
            val infoText = getString(R.string.no_internet_connection_info)
            setErrorView(viewModel, show, titleText, infoText)
        })
    }

    private fun setUpGeneralError(viewModel: PlanetsViewModel) {
        viewModel.showGeneralError().observe(this, NonNullObserver { show ->
            val titleText = getString(R.string.error)
            val infoText = getString(R.string.error_info)
            setErrorView(viewModel, show, titleText, infoText)
        })
    }

    private fun setUpFavouritesListener(viewModel: PlanetsViewModel) {
        viewModel.goToFavouritesEvent().observe(this, NonNullObserver { event ->
            event.invokeIfNotHandled { goToFavourites() }
        })

        favouritesTv.setOnClickListener {
            viewModel.favouritesClicked()
        }
    }

    private fun setUpSearchListener(viewModel: PlanetsViewModel) {
        viewModel.goToSearchEvent().observe(this, NonNullObserver { event ->
            event.invokeIfNotHandled { goToSearch() }
        })

        searchTv.setOnClickListener {
            viewModel.searchClicked()
        }
    }

    private fun setErrorView(
            viewModel: PlanetsViewModel,
            show: Boolean,
            titleText: String,
            infoText: String
    ) {

        if (show) {
            errorView.visibility = View.VISIBLE
            errorView.setTitleText(titleText)
            errorView.setInfoText(infoText)
            errorView.setRetryListener {
                viewModel.retryClicked()
            }
        } else {
            errorView.visibility = View.GONE
        }
    }

    private fun goToPlanetDetail(planet: Planet, planetImageIv: ImageView) {
        val imageTransitionName = ViewCompat.getTransitionName(planetImageIv)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                planetImageIv,
                imageTransitionName
        )

        Intent(this, PlanetDetailActivity::class.java)
                .putExtra(Constants.PLANET, planet)
                .run { startActivity(this, options.toBundle()) }
    }

    private fun goToFavourites() {
        Intent(this, FavouritesActivity::class.java)
                .run { startActivity(this) }
    }

    private fun goToSearch() {
        Intent(this, SearchActivity::class.java)
                .run { startActivity(this) }
    }
}
