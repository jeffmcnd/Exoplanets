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
import com.aidanlaing.exoplanets.data.planets.PlanetImage
import com.aidanlaing.exoplanets.screens.planetdetail.PlanetDetailActivity
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
    }

    private fun setUpPlanets(viewModel: PlanetsViewModel) {
        val planetsAdapter = PlanetsAdapter({ planet, planetImage, planetImageIv ->
            goToPlanetDetail(planet, planetImage, planetImageIv)
        })

        val layoutManager = LinearLayoutManager(this)

        planetsRv.adapter = planetsAdapter
        planetsRv.layoutManager = layoutManager

        viewModel.getPlanets().observe(this, NonNullObserver { planets ->
            planetsAdapter.replaceItems(planets)
        })
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

    private fun goToPlanetDetail(
            planet: Planet,
            planetImage: PlanetImage,
            planetImageIv: ImageView
    ) {

        val imageTransitionName = ViewCompat.getTransitionName(planetImageIv)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                planetImageIv,
                imageTransitionName
        )

        Intent(this, PlanetDetailActivity::class.java)
                .putExtra(Constants.PLANET, planet)
                .putExtra(Constants.PLANET_IMAGE, planetImage)
                .putExtra(Constants.IMAGE_TRANSITION_NAME, imageTransitionName)
                .run { startActivity(this, options.toBundle()) }
    }
}
