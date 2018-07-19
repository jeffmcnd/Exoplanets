package com.aidanlaing.exoplanets.screens.confirmedplanetdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.Constants
import com.aidanlaing.exoplanets.common.Injector
import com.aidanlaing.exoplanets.common.glide.ColorTransformation
import com.aidanlaing.exoplanets.data.confirmedplanets.PlanetImage
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_confirmed_planet_detail.*

class ConfirmedPlanetDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmed_planet_detail)

        val planetName = intent.getStringExtra(Constants.PLANET_NAME)
        if (planetName == null) {
            showToast(getString(R.string.error_unexpected))
            finish()
            return
        }

        val viewModel = ViewModelProviders
                .of(this, Injector.provideViewModelFactory(this))
                .get(ConfirmedPlanetDetailViewModel::class.java)

        setUpConfirmedPlanet(viewModel, planetName)
        setUpLoading(viewModel)
        setUpNoConnection(viewModel, planetName)
        setUpGeneralError(viewModel, planetName)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpConfirmedPlanet(viewModel: ConfirmedPlanetDetailViewModel, planetName: String) {
        viewModel.getConfirmedPlanet(planetName).observe(this, Observer { confirmedPlanet ->
            confirmedPlanet?.let {

                val planetImage = confirmedPlanet.getPlanetImage()
                val resId = when (planetImage) {
                    is PlanetImage.Stripe -> R.drawable.ic_planet_stripe
                    is PlanetImage.Crevice -> R.drawable.ic_planet_crevice
                    is PlanetImage.WaterLand -> R.drawable.ic_planet_water_land
                }

                Glide.with(this)
                        .load(resId)
                        .apply(RequestOptions.bitmapTransform(ColorTransformation(planetImage.color)))
                        .into(planetImageIv)

                planetNameTv.text = confirmedPlanet.planetName
            }
        })
    }

    private fun setUpLoading(viewModel: ConfirmedPlanetDetailViewModel) {
        viewModel.showLoading().observe(this, Observer { show ->
            show?.let {
                if (show) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        })
    }

    private fun setUpNoConnection(viewModel: ConfirmedPlanetDetailViewModel, planetName: String) {
        viewModel.showNoConnection().observe(this, Observer { show ->
            show?.let {
                val titleText = getString(R.string.no_internet_connection)
                val infoText = getString(R.string.no_internet_connection_info)
                setError(viewModel, planetName, show, titleText, infoText)
            }
        })
    }

    private fun setUpGeneralError(viewModel: ConfirmedPlanetDetailViewModel, planetName: String) {
        viewModel.showGeneralError().observe(this, Observer { show ->
            show?.let {
                val titleText = getString(R.string.error)
                val infoText = getString(R.string.error_info)
                setError(viewModel, planetName, show, titleText, infoText)
            }
        })
    }

    private fun setError(
            viewModel: ConfirmedPlanetDetailViewModel,
            planetName: String,
            show: Boolean,
            titleText: String,
            infoText: String
    ) {
        if (show) {
            errorView.visibility = View.VISIBLE
            errorView.setTitleText(titleText)
            errorView.setInfoText(infoText)
            errorView.setRetryListener {
                viewModel.retryClicked(planetName)
            }
        } else {
            errorView.visibility = View.GONE
        }
    }
}
