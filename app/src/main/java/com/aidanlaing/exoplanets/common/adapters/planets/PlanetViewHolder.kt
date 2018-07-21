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
                    planetImage: PlanetImage,
                    planetImageIv: ImageView
            ) -> Unit
    ) = with(itemView) {

        ViewCompat.setTransitionName(planetImageIv, "image")

        val planetImage = planet.getPlanetImage()
        GlideApp.with(this)
                .load(planetImage.resId)
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
            planetClickListener(planet, planetImage, planetImageIv)
        }
    }

    companion object {
        fun inflate(parent: ViewGroup) = PlanetViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_planet, parent, false)
        )
    }

}