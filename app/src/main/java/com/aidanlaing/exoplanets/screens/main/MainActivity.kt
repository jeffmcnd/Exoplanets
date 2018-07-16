package com.aidanlaing.exoplanets.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.Injector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textTv.text = "Start text"

        val viewModel = ViewModelProviders.of(
                this,
                Injector.provideViewModelFactory(this)
        ).get(MainViewModel::class.java)

        viewModel.getConfirmedPlanets().observe(this, Observer { confirmedPlanets ->
            textTv.text = confirmedPlanets.toString()
        })

        viewModel.getConfirmedPlanetsSize().observe(this, Observer { size ->
            textTv.text = size
        })
    }
}
