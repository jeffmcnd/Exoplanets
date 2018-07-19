package com.aidanlaing.exoplanets.screens.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.Constants
import com.aidanlaing.exoplanets.common.Injector
import com.aidanlaing.exoplanets.common.adapters.confirmedplanets.ConfirmedPlanetsAdapter
import com.aidanlaing.exoplanets.screens.confirmedplanetdetail.ConfirmedPlanetDetailActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders
                .of(this, Injector.provideViewModelFactory(this))
                .get(MainViewModel::class.java)

        setUpConfirmedPlanets(viewModel)
        setUpLoading(viewModel)
        setUpNoConnection(viewModel)
        setUpGeneralError(viewModel)
    }

    private fun setUpConfirmedPlanets(viewModel: MainViewModel) {

        val confirmedPlanetsAdapter = ConfirmedPlanetsAdapter({ confirmedPlanet ->
            goToConfirmedPlanetDetail(confirmedPlanet.planetName)
        })

        val layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
        )

        confirmedPlanetsRv.adapter = confirmedPlanetsAdapter
        confirmedPlanetsRv.layoutManager = layoutManager

        viewModel.getConfirmedPlanets().observe(this, Observer { confirmedPlanets ->
            confirmedPlanets?.let { confirmedPlanetsAdapter.replaceItems(confirmedPlanets) }
        })
    }

    private fun setUpLoading(viewModel: MainViewModel) {
        viewModel.showLoading().observe(this, Observer { show ->
            show?.let {
                if (show) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        })
    }

    private fun setUpNoConnection(viewModel: MainViewModel) {
        viewModel.showNoConnection().observe(this, Observer { show ->
            show?.let {
                val titleText = getString(R.string.no_internet_connection)
                val infoText = getString(R.string.no_internet_connection_info)
                setError(viewModel, show, titleText, infoText)
            }
        })
    }

    private fun setUpGeneralError(viewModel: MainViewModel) {
        viewModel.showGeneralError().observe(this, Observer { show ->
            show?.let {
                val titleText = getString(R.string.error)
                val infoText = getString(R.string.error_info)
                setError(viewModel, show, titleText, infoText)
            }
        })
    }

    private fun setError(
            viewModel: MainViewModel,
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

    private fun goToConfirmedPlanetDetail(planetName: String) {
        Intent(this, ConfirmedPlanetDetailActivity::class.java)
                .putExtra(Constants.PLANET_NAME, planetName)
                .run { startActivity(this) }
    }
}
