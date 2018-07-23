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
        setPlanetData(planet)
        setUpBackListener()
        setUpFavouriteListener(viewModel, planet)
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

    private fun setPlanetData(planet: Planet) {
        planetNameTv.text = planet.name

        val distance = planet.getDistanceParsecs().defaultIfBlank(getString(R.string.unknown))
        planetDistanceTv.text = getString(
                R.string.formatted_parsecs_away,
                distance
        )
    }

    private fun setUpBackListener() {
        backIv.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpFavouriteListener(viewModel: PlanetDetailViewModel, planet: Planet) {
        viewModel.isFavourite(planet).observe(this, NonNullObserver { favourite ->
            val resId = if (favourite) R.drawable.ic_heart_filled_white_24dp
            else R.drawable.ic_heart_outline_white_24dp
            favouriteIv.setImageResource(resId)
        })

        favouriteIv.setOnClickListener {
            viewModel.favouriteClicked(planet)
        }
    }
}
