package com.aidanlaing.exoplanets.common.adapters.confirmedplanets

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.glide.ColorTransformation
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanet
import com.aidanlaing.exoplanets.data.confirmedplanets.PlanetImage
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_confirmed_planet.view.*

class ConfirmedPlanetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
            confirmedPlanet: ConfirmedPlanet,
            confirmedPlanetClickListener: (confirmedPlanet: ConfirmedPlanet) -> Unit
    ) = with(itemView) {

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

        layout.setOnClickListener {
            confirmedPlanetClickListener(confirmedPlanet)
        }
    }

    companion object {
        fun inflate(parent: ViewGroup) = ConfirmedPlanetViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_confirmed_planet, parent, false)
        )
    }

}