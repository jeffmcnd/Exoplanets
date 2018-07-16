package com.aidanlaing.exoplanets.screens.confirmedplanetdetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.Constants
import com.aidanlaing.exoplanets.common.Injector
import kotlinx.android.synthetic.main.activity_confirmed_planet_detail.*

class ConfirmedPlanetDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmed_planet_detail)

        progressBar.visibility = View.GONE
        noConnectionView.visibility = View.GONE
        errorView.visibility = View.GONE

        val planetName = intent.getStringExtra(Constants.PLANET_NAME)
        if (planetName == null) {
            showToast(getString(R.string.error_unexpected))
            finish()
            return
        }

        val viewModel = ViewModelProviders
                .of(this, Injector.provideViewModelFactory(this))
                .get(ConfirmedPlanetDetailViewModel::class.java)

        viewModel.getConfirmedPlanet(planetName).observe(this, Observer { confirmedPlanet ->
            planetNameTv.text = confirmedPlanet.planetName
        })

        viewModel.showLoading().observe(this, Observer { show ->
            if (show) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.GONE
        })

        viewModel.showNoConnection().observe(this, Observer { show ->
            if (show) noConnectionView.visibility = View.VISIBLE
            else noConnectionView.visibility = View.GONE
        })

        viewModel.showGeneralError().observe(this, Observer { show ->
            if (show) {
                errorView.visibility = View.VISIBLE
                errorView.setTitleText(getString(R.string.error))
                errorView.setInfoText(getString(R.string.error_info))
                errorView.setRetryListener {
                    viewModel.retryClicked(planetName)
                }
            } else {
                errorView.visibility = View.GONE
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
