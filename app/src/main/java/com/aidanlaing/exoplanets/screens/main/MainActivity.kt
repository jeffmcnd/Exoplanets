package com.aidanlaing.exoplanets.screens.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.Injector
import com.aidanlaing.exoplanets.common.adapters.confirmedplanets.ConfirmedPlanetsAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar.visibility = View.GONE
        noConnectionView.visibility = View.GONE
        errorView.visibility = View.GONE

        val confirmedPlanetsAdapter = ConfirmedPlanetsAdapter()
        confirmedPlanetsRv.adapter = confirmedPlanetsAdapter
        confirmedPlanetsRv.layoutManager = LinearLayoutManager(this)

        val viewModel = ViewModelProviders.of(
                this,
                Injector.provideViewModelFactory(this)
        ).get(MainViewModel::class.java)

        viewModel.getConfirmedPlanets().observe(this, Observer { confirmedPlanets ->
            confirmedPlanetsAdapter.replaceItems(confirmedPlanets)
        })

        viewModel.showGeneralError().observe(this, Observer { show ->
            if (show) {
                errorView.visibility = View.VISIBLE
                errorView.setTitleText(getString(R.string.error))
                errorView.setInfoText(getString(R.string.error_info))
                errorView.setRetryListener {
                    viewModel.retryClicked()
                }
            } else {
                errorView.visibility = View.GONE
            }
        })

        viewModel.showNoConnection().observe(this, Observer { show ->
            if (show) noConnectionView.visibility = View.VISIBLE
            else noConnectionView.visibility = View.GONE
        })

        viewModel.showLoading().observe(this, Observer { show ->
            if (show) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.GONE
        })
    }
}
