package com.aidanlaing.exoplanets.screens.planets

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.Constants
import com.aidanlaing.exoplanets.common.Injector
import com.aidanlaing.exoplanets.common.adapters.planets.PlanetsAdapter
import com.aidanlaing.exoplanets.common.extensions.dpToPx
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
        setUpShowActions(viewModel)
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

        viewModel.getPlanets().observe(this, NonNullObserver { planets ->
            planetsAdapter.replaceItems(planets)
        })

        planetsRv.layoutManager = layoutManager
        planetsRv.adapter = planetsAdapter

        planetsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                viewModel.listScrolled(dy)
            }
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

    private fun setUpFavouritesListener(viewModel: PlanetsViewModel) {
        viewModel.goToFavouritesEvent().observe(this, NonNullObserver { event ->
            event.invokeIfNotHandled { goToFavourites() }
        })

        favouritesCv.setOnClickListener {
            viewModel.favouritesClicked()
        }
    }

    private fun setUpSearchListener(viewModel: PlanetsViewModel) {
        viewModel.goToSearchEvent().observe(this, NonNullObserver { event ->
            event.invokeIfNotHandled { goToSearch() }
        })

        searchCv.setOnClickListener {
            viewModel.searchClicked()
        }
    }

    private fun setUpShowActions(viewModel: PlanetsViewModel) {
        viewModel.showActions().observe(this, NonNullObserver { show ->
            val constraintSet = ConstraintSet()
            constraintSet.clone(layout)

            if (show) {
                val margin = 16.dpToPx

                constraintSet.clear(R.id.searchCv, ConstraintSet.BOTTOM)
                constraintSet.connect(
                        R.id.searchCv,
                        ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.TOP
                )
                constraintSet.setMargin(R.id.searchCv, ConstraintSet.TOP, margin)

                constraintSet.clear(R.id.favouritesCv, ConstraintSet.TOP)
                constraintSet.connect(
                        R.id.favouritesCv,
                        ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.BOTTOM
                )
                constraintSet.setMargin(R.id.favouritesCv, ConstraintSet.BOTTOM, margin)
            } else {
                constraintSet.clear(R.id.searchCv, ConstraintSet.TOP)
                constraintSet.connect(
                        R.id.searchCv,
                        ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.TOP
                )

                constraintSet.clear(R.id.favouritesCv, ConstraintSet.BOTTOM)
                constraintSet.connect(
                        R.id.favouritesCv,
                        ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.BOTTOM
                )
            }

            val transition = ChangeBounds()
            transition.interpolator = LinearInterpolator()
            transition.duration = 200
            TransitionManager.beginDelayedTransition(layout, transition)
            constraintSet.applyTo(layout)
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
