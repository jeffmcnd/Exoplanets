package com.aidanlaing.exoplanets.screens.search

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.Constants
import com.aidanlaing.exoplanets.common.Injector
import com.aidanlaing.exoplanets.common.adapters.planets.PlanetsAdapter
import com.aidanlaing.exoplanets.common.livedata.NonNullObserver
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.screens.planetdetail.PlanetDetailActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val viewModel = ViewModelProviders
                .of(this, Injector.provideViewModelFactory(this))
                .get(SearchViewModel::class.java)

        setUpBackListener(viewModel)
        setUpSearchListener(viewModel)
        setUpPlanets(viewModel)
        setUpLoading(viewModel)
        setUpNoConnection(viewModel)
        setUpGeneralError(viewModel)
    }

    private fun setUpBackListener(viewModel: SearchViewModel) {
        viewModel.onBackEvent().observe(this, NonNullObserver { event ->
            event.invokeIfNotHandled {
                onBackPressed()
            }
        })

        backIv.setOnClickListener {
            viewModel.backClicked()
        }
    }

    private fun setUpSearchListener(viewModel: SearchViewModel) {
        searchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchClicked(searchEt.text.toString())
                true
            } else {
                false
            }
        }

        searchIv.setOnClickListener {
            viewModel.searchClicked(searchEt.text.toString())
        }
    }

    private fun setUpPlanets(viewModel: SearchViewModel) {
        val planetsAdapter = PlanetsAdapter({ planetClick ->
            viewModel.planetClicked(planetClick)
        })

        val layoutManager = LinearLayoutManager(this)

        viewModel.goToDetailEvent().observe(this, NonNullObserver { event ->
            event.invokeIfNotHandled { data ->
                goToPlanetDetail(data.planet, data.planetImageView)
            }
        })

        viewModel.getPlanets().observe(this, NonNullObserver { planets ->
            planetsAdapter.replaceItems(planets)
        })

        planetsRv.layoutManager = layoutManager
        planetsRv.adapter = planetsAdapter
    }

    private fun setUpLoading(viewModel: SearchViewModel) {
        viewModel.showLoading().observe(this, NonNullObserver { show ->
            if (show) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.GONE
        })
    }

    private fun setUpNoConnection(viewModel: SearchViewModel) {
        viewModel.showNoConnection().observe(this, NonNullObserver { show ->
            val titleText = getString(R.string.no_internet_connection)
            val infoText = getString(R.string.no_internet_connection_info)
            setErrorView(viewModel, show, titleText, infoText)
        })
    }

    private fun setUpGeneralError(viewModel: SearchViewModel) {
        viewModel.showGeneralError().observe(this, NonNullObserver { show ->
            val titleText = getString(R.string.error)
            val infoText = getString(R.string.error_info)
            setErrorView(viewModel, show, titleText, infoText)
        })
    }

    private fun setErrorView(
            viewModel: SearchViewModel,
            show: Boolean,
            titleText: String,
            infoText: String
    ) {
        if (show) {
            errorView.visibility = View.VISIBLE
            errorView.setTitleText(titleText)
            errorView.setInfoText(infoText)
            errorView.setRetryListener {
                viewModel.retryClicked(searchEt.text.toString())
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
