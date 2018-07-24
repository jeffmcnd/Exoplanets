package com.aidanlaing.exoplanets.screens.favourites

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
import com.aidanlaing.exoplanets.screens.planetdetail.PlanetDetailActivity
import kotlinx.android.synthetic.main.activity_favourites.*

class FavouritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        val viewModel = ViewModelProviders
                .of(this, Injector.provideViewModelFactory(this))
                .get(FavouritesViewModel::class.java)

        setUpPlanets(viewModel)
        setUpBackListener(viewModel)
        setUpLoading(viewModel)
        setUpGeneralError(viewModel)
    }

    private fun setUpBackListener(viewModel: FavouritesViewModel) {
        viewModel.onBackEvent().observe(this, NonNullObserver { event ->
            event.invoke {
                onBackPressed()
            }
        })

        backIv.setOnClickListener {
            viewModel.backClicked()
        }
    }

    private fun setUpPlanets(viewModel: FavouritesViewModel) {
        val planetsAdapter = PlanetsAdapter({ planetClick ->
            layout.requestFocus()
            viewModel.planetClicked(planetClick)
        })

        val layoutManager = LinearLayoutManager(this)

        viewModel.goToDetailEvent().observe(this, NonNullObserver { event ->
            event.invokeWithData { data ->
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

    private fun setUpLoading(viewModel: FavouritesViewModel) {
        viewModel.showLoading().observe(this, NonNullObserver { show ->
            if (show) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.GONE
        })
    }

    private fun setUpGeneralError(viewModel: FavouritesViewModel) {
        viewModel.showGeneralError().observe(this, NonNullObserver { show ->
            val titleText = getString(R.string.error)
            val infoText = getString(R.string.error_info)
            setErrorView(viewModel, show, titleText, infoText)
        })
    }

    private fun setErrorView(
            viewModel: FavouritesViewModel,
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
}
