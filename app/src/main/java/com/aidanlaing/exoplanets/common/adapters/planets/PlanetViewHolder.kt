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