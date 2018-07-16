package com.aidanlaing.exoplanets.screens.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
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

        val confirmedPlanetsAdapter = ConfirmedPlanetsAdapter()
        confirmedPlanetsRv.adapter = confirmedPlanetsAdapter
        confirmedPlanetsRv.layoutManager = LinearLayoutManager(this)

        val viewModel = ViewModelProviders.of(
                this,
                Injector.provideViewModelFactory(this)
        ).get(MainViewModel::class.java)

        viewModel.getConfirmedPlanets(true).observe(this, Observer { confirmedPlanets ->
            confirmedPlanetsAdapter.replaceItems(confirmedPlanets)
        })

        viewModel.getError().observe(this, Observer { exception ->
            showToast(exception.localizedMessage)
        })

        viewModel.getLoading().observe(this, Observer { loading ->
            if (loading) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.GONE
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
