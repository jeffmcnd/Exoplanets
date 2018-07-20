package com.aidanlaing.exoplanets.common.adapters.planets

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.glide.ColorTransformation
import com.aidanlaing.exoplanets.common.glide.GlideApp
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.data.planets.PlanetImage
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_planet.view.*

class PlanetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
            planet: Planet,
            planetClickListener: (planet: Planet) -> Unit
    ) = with(itemView) {

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

        layout.setOnClickListener {
            planetClickListener(planet)
        }
    }

    companion object {
        fun inflate(parent: ViewGroup) = PlanetViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_planet, parent, false)
        )
    }

}