package com.aidanlaing.exoplanets.common.adapters.planets

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.glide.ColorTransformation
import com.aidanlaing.exoplanets.common.glide.GlideApp
import com.aidanlaing.exoplanets.data.planets.Planet
import com.aidanlaing.exoplanets.data.planets.PlanetImage
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_planet.view.*
import kotlin.math.roundToInt

class PlanetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
            planet: Planet,
            planetClickListener: (
                    planet: Planet,
                    planetImageIv: ImageView
            ) -> Unit
    ) = with(itemView) {

        ViewCompat.setTransitionName(planetImageIv, "image")

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

        planetNameTv.text = planet.name

        val distance = if (planet.starDistanceParsecs != null) {
            planet.starDistanceParsecs.roundToInt().toString()
        } else {
            context.getString(R.string.unknown)
        }

        planetDistanceTv.text = context.getString(
                R.string.formatted_parsecs_away,
                distance
        )

        layout.setOnClickListener {
            planetClickListener(planet, planetImageIv)
        }
    }

    companion object {
        fun inflate(parent: ViewGroup) = PlanetViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_planet, parent, false)
        )
    }

}