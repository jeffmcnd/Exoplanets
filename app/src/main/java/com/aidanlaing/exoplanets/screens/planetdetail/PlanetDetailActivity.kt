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
        val imageTransitionName = intent.getStringExtra(Constants.IMAGE_TRANSITION_NAME)
        if (planet == null || imageTransitionName == null) {
            showToast(getString(R.string.error_unexpected))
            finish()
            return
        }

        val viewModel = ViewModelProviders
                .of(this, Injector.provideViewModelFactory(this))
                .get(PlanetDetailViewModel::class.java)

        planetNameTv.text = planet.name

        startEnterTransition(planet, imageTransitionName)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startEnterTransition(planet: Planet, imageTransitionName: String) {
        postponeEnterTransition()
        planetImageIv.transitionName = imageTransitionName

        val planetImage = planet.getPlanetImage()
        val resId = when (planetImage) {
            is PlanetImage.PlanetOne -> R.drawable.ic_planet_1
            is PlanetImage.PlanetTwo -> R.drawable.ic_planet_2
            is PlanetImage.PlanetThree -> R.drawable.ic_planet_3
            is PlanetImage.PlanetFour -> R.drawable.ic_planet_4
            is PlanetImage.PlanetFive -> R.drawable.ic_planet_5
            is PlanetImage.PlanetSix -> R.drawable.ic_planet_6
        }

        GlideApp.with(this)
                .load(resId)
                .apply(RequestOptions.bitmapTransform(ColorTransformation(planetImage.color)))
                .listener(GlideListener<Drawable>(
                        { startPostponedEnterTransition() },
                        { startPostponedEnterTransition() }
                ))
                .into(planetImageIv)
    }
}
