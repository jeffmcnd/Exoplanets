package com.aidanlaing.exoplanets.screens.planetdetail

import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.Constants
import com.aidanlaing.exoplanets.common.Injector
import com.aidanlaing.exoplanets.common.extensions.defaultIfBlank
import com.aidanlaing.exoplanets.common.extensions.setFormattedTextWithDefaultIfBlank
import com.aidanlaing.exoplanets.common.glide.ColorTransformation
import com.aidanlaing.exoplanets.common.glide.GlideApp
import com.aidanlaing.exoplanets.common.glide.GlideListener
import com.aidanlaing.exoplanets.common.livedata.NonNullObserver
import com.aidanlaing.exoplanets.data.planets.Planet
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_planet_detail.*

class PlanetDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet_detail)

        val planet = intent.getParcelableExtra<Planet?>(Constants.PLANET)
        if (planet == null) {
            showToast(getString(R.string.error_unexpected))
            finish()
            return
        }

        val viewModel = ViewModelProviders
                .of(this, Injector.provideViewModelFactory(this))
                .get(PlanetDetailViewModel::class.java)

        startEnterTransition(planet)
        setUpBackListener(viewModel)
        setUpFavouriteListener(viewModel, planet)
        setPlanetData(planet)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startEnterTransition(planet: Planet) {
        planetImageIv.transitionName = planet.name

        postponeEnterTransition()

        val planetImage = planet.getPlanetImage()
        GlideApp.with(this)
                .load(planetImage.resId)
                .apply(RequestOptions.bitmapTransform(ColorTransformation(planetImage.color)))
                .listener(GlideListener<Drawable>(
                        onSuccess = {
                            startPostponedEnterTransition()
                        },
                        onError = {
                            startPostponedEnterTransition()
                        }))
                .into(planetImageIv)
    }

    private fun setUpBackListener(viewModel: PlanetDetailViewModel) {
        viewModel.onBackEvent().observe(this, NonNullObserver { event ->
            event.invokeIfNotHandled {
                onBackPressed()
            }
        })

        backIv.setOnClickListener {
            viewModel.backClicked()
        }
    }

    private fun setUpFavouriteListener(viewModel: PlanetDetailViewModel, planet: Planet) {
        viewModel.isFavourite(planet).observe(this, NonNullObserver { isFavourite ->
            val resId = if (isFavourite) R.drawable.ic_heart_filled_white_24dp
            else R.drawable.ic_heart_outline_white_24dp
            favouriteIv.setImageResource(resId)
        })

        favouriteIv.setOnClickListener {
            viewModel.favouriteClicked(planet)
        }
    }

    private fun setPlanetData(planet: Planet) {
        planetNameTv.text = planet.name

        val planetDistance = planet.getDistanceParsecsOrBlank()
                .defaultIfBlank(getString(R.string.unknown))
        planetDistanceValueTv.text = getString(
                R.string.formatted_parsecs_away,
                planetDistance
        )

        planetDiscoveryYearValueTv.text = planet.discoveryYear

        val planetDiscoveryMethod = planet.getDiscoveryMethodOrBlank()
                .defaultIfBlank(getString(R.string.unknown))
        planetDiscoveryMethodValueTv.text = planetDiscoveryMethod

        val planetRadius = planet.getJupiterRadiusOrBlank()
        planetRadiusValueTv.setFormattedTextWithDefaultIfBlank(
                planetRadius,
                getString(R.string.formatted_times_jupiter, planetRadius),
                getString(R.string.unknown)
        )

        val planetMass = planet.getJupiterMassOrBlank()
        planetMassValueTv.setFormattedTextWithDefaultIfBlank(
                planetMass,
                getString(R.string.formatted_times_jupiter, planetMass),
                getString(R.string.unknown)
        )

        val planetDensity = planet.getDensityOrBlank()
        planetDensityValueTv.setFormattedTextWithDefaultIfBlank(
                planetDensity,
                getString(R.string.formatted_g_per_cm_cubed, planetDensity),
                getString(R.string.unknown)
        )

        val orbitalPeriodDays = planet.getOrbitalPeriodDaysOrBlank()
        planetOrbitalPeriodValueTv.setFormattedTextWithDefaultIfBlank(
                orbitalPeriodDays,
                getString(R.string.formatted_days, orbitalPeriodDays),
                getString(R.string.unknown)
        )

        val starName = planet.getStarNameOrBlank()
                .defaultIfBlank(getString(R.string.unknown))
        starNameValueTv.text = starName

        val starTemp = planet.getStarTempKelvinOrBlank()
        starTempValueTv.setFormattedTextWithDefaultIfBlank(
                starTemp,
                getString(R.string.formatted_kelvin, starTemp),
                getString(R.string.unknown)
        )

        val starRadius = planet.getStarSunRadiusOrBlank()
        starRadiusValueTv.setFormattedTextWithDefaultIfBlank(
                starRadius,
                getString(R.string.formatted_times_sun, starRadius),
                getString(R.string.unknown)
        )

        val starMass = planet.getStarSunMassOrBlank()
        starMassValueTv.setFormattedTextWithDefaultIfBlank(
                starMass,
                getString(R.string.formatted_times_sun, starMass),
                getString(R.string.unknown)
        )

        val numberOfPlants = planet.getNumPlanetsInSystemOrBlank()
                .defaultIfBlank(getString(R.string.unknown))
        starNumberOfPlanetsValueTv.text = numberOfPlants
    }
}
