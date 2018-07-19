package com.aidanlaing.exoplanets.common.adapters.confirmedplanets

import android.graphics.Color
import android.support.v4.graphics.ColorUtils
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.common.glide.ColorTransformation
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanet
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_confirmed_planet.view.*
import java.util.*

class ConfirmedPlanetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
            confirmedPlanet: ConfirmedPlanet,
            confirmedPlanetClickListener: (confirmedPlanet: ConfirmedPlanet) -> Unit
    ) = with(itemView) {

        val seed = confirmedPlanet.planetName.hashCode().toLong()
        val random = Random(seed)
        val color = generateRandomPastelColor(random)

        val resId = when (random.nextInt(3)) {
            0 -> R.drawable.ic_planet_water_land
            1 -> R.drawable.ic_planet_crevice
            2 -> R.drawable.ic_planet_stripe
            else -> R.drawable.ic_planet_water_land
        }

        Glide.with(this)
                .load(resId)
                .apply(RequestOptions.bitmapTransform(ColorTransformation(color)))
                .into(planetImageIv)

        planetNameTv.text = confirmedPlanet.planetName

        layout.setOnClickListener {
            confirmedPlanetClickListener(confirmedPlanet)
        }
    }

    fun generateRandomPastelColor(random: Random): Int {
        val h = 0f + random.nextInt(360)
        val s = 42f + random.nextInt(98)
        val v = 40f + random.nextInt(90)
        return Color.HSVToColor(100, floatArrayOf(h, s, v))
    }

    companion object {
        fun inflate(parent: ViewGroup) = ConfirmedPlanetViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_confirmed_planet, parent, false)
        )
    }

}