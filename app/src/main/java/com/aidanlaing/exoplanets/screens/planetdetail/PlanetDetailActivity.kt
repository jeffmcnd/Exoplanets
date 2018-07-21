package com.aidanlaing.exoplanets.screens.planetdetail

import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.Constants
import com.aidanlaing.exoplanets.common.Injector
import com.aidanlaing.exoplanets.common.glide.ColorTransformation
import com.aidanlaing.exoplanets.common.glide.GlideApp
import com.aidanlaing.exoplanets.common.glide.GlideListener
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.data.planets.PlanetImage
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_planet_detail.*

class PlanetDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet_detail)

        val planet = intent.getParcelableExtra<Planet?>(Constants.PLANET)
        val planetImage = intent.getParcelableExtra<PlanetImage?>(Constants.PLANET_IMAGE)
        val imageTransitionName = intent.getStringExtra(Constants.IMAGE_TRANSITION_NAME)
        if (planet == null || planetImage == null || imageTransitionName == null) {
            showToast(getString(R.string.error_unexpected))
            finish()
            return
        }

        val viewModel = ViewModelProviders
                .of(this, Injector.provideViewModelFactory(this))
                .get(PlanetDetailViewModel::class.java)

        startEnterTransition(planetImage, imageTransitionName)
        showPlanetData(planet)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startEnterTransition(
            planetImage: PlanetImage,
            imageTransitionName: String
    ) {
        postponeEnterTransition()

        planetImageIv.transitionName = imageTransitionName

        GlideApp.with(this)
                .load(planetImage.resId)
                .apply(RequestOptions.bitmapTransform(ColorTransformation(planetImage.color)))
                .listener(GlideListener<Drawable>({
                    startPostponedEnterTransition()
                }, {
                    startPostponedEnterTransition()
                }))
                .into(planetImageIv)
    }

    private fun showPlanetData(planet: Planet) {
        planetNameTv.text = planet.name

        val distance = if (planet.starDistanceParsecs != null) {
            planet.starDistanceParsecs.toString()
        } else {
            getString(R.string.unknown)
        }

        planetDistanceTv.text = getString(
                R.string.formatted_parsecs_away,
                distance
        )
    }
}
