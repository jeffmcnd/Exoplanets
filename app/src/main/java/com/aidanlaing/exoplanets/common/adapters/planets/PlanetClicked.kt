package com.aidanlaing.exoplanets.common.adapters.planets

import android.widget.ImageView
import com.aidanlaing.exoplanets.data.planets.Planet

data class PlanetClicked(
        val planet: Planet,
        val planetImageView: ImageView
)