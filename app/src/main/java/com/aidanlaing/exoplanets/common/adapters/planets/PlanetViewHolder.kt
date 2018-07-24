package com.aidanlaing.exoplanets.common.adapters.planets

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.extensions.defaultIfBlank
import com.aidanlaing.exoplanets.common.glide.ColorTransformation
import com.aidanlaing.exoplanets.common.glide.GlideApp
import com.aidanlaing.exoplanets.data.planets.Planet
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_planet.view.*

class PlanetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
            planet: Planet,
            planetClickListener: (planetClick: PlanetClick) -> Unit
    ) = with(itemView) {

        layout.setOnClickListener {
            planetClickListener(PlanetClick(planet, planetImageIv))
        }

        ViewCompat.setTransitionName(planetImageIv, planet.name)
        val planetImage = planet.getPlanetImage()
        GlideApp.with(this)
                .load(planetImage.resId)
                .apply(RequestOptions.bitmapTransform(ColorTransformation(planetImage.color)))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(planetImageIv)

        planetNameTv.text = planet.name

        val distance = planet.getRoundedDistanceParsecs()
                .defaultIfBlank(context.getString(R.string.unknown))
        planetDistanceTv.text = context.getString(
                R.string.formatted_parsecs_away,
                distance
        )

        planetPublishedDateTv.text = context.getString(
                R.string.formatted_discovered_in,
                planet.discoveryYear
        )
    }

    companion object {
        fun inflate(parent: ViewGroup) = PlanetViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_planet, parent, false)
        )
    }

}