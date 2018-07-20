package com.aidanlaing.exoplanets.screens.planetdetail

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
import com.aidanlaing.exoplanets.common.glide.GlideApp
import com.aidanlaing.exoplanets.data.planets.PlanetImage
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_planet_detail.*

class PlanetDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet_detail)

        val planetName = intent.getStringExtra(Constants.PLANET_NAME)
        if (planetName == null) {
            showToast(getString(R.string.error_unexpected))
            finish()
            return
        }

        val viewModel = ViewModelProviders
                .of(this, Injector.provideViewModelFactory(this))
                .get(PlanetDetailViewModel::class.java)

        setUpPlanet(viewModel, planetName)
        setUpLoading(viewModel)
        setUpNoConnection(viewModel, planetName)
        setUpGeneralError(viewModel, planetName)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpPlanet(viewModel: PlanetDetailViewModel, planetName: String) {
        viewModel.getPlanet(planetName).observe(this, Observer { planet ->
            planet?.let {
                val planetImage = planet.getPlanetImage()
                val resId = when (planetImage) {
                    is PlanetImage.GasOne -> R.drawable.ic_planet_gas_1
                    is PlanetImage.GasTwo -> R.drawable.ic_planet_gas_2
                    is PlanetImage.GasThree -> R.drawable.ic_planet_gas_3
                    is PlanetImage.RockyOne -> R.drawable.ic_planet_rocky_1
                    is PlanetImage.RockyTwo -> R.drawable.ic_planet_rocky_2
                    is PlanetImage.RockyThree -> R.drawable.ic_planet_rocky_3
                    is PlanetImage.WaterLandOne -> R.drawable.ic_planet_water_land_1
                    is PlanetImage.WaterLandTwo -> R.drawable.ic_planet_water_land_2
                    is PlanetImage.WaterLandThree -> R.drawable.ic_planet_water_land_3
                }

                GlideApp.with(this)
                        .load(resId)
                        .apply(RequestOptions.bitmapTransform(ColorTransformation(planetImage.color)))
                        .into(planetImageIv)

                planetNameTv.text = planet.planetName
            }
        })
    }

    private fun setUpLoading(viewModel: PlanetDetailViewModel) {
        viewModel.showLoading().observe(this, Observer { show ->
            show?.let {
                if (show) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        })
    }

    private fun setUpNoConnection(viewModel: PlanetDetailViewModel, planetName: String) {
        viewModel.showNoConnection().observe(this, Observer { show ->
            show?.let {
                val titleText = getString(R.string.no_internet_connection)
                val infoText = getString(R.string.no_internet_connection_info)
                setError(viewModel, planetName, show, titleText, infoText)
            }
        })
    }

    private fun setUpGeneralError(viewModel: PlanetDetailViewModel, planetName: String) {
        viewModel.showGeneralError().observe(this, Observer { show ->
            show?.let {
                val titleText = getString(R.string.error)
                val infoText = getString(R.string.error_info)
                setError(viewModel, planetName, show, titleText, infoText)
            }
        })
    }

    private fun setError(
            viewModel: PlanetDetailViewModel,
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
