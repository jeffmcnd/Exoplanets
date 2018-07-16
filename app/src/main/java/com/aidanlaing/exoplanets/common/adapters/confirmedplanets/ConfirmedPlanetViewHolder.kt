package com.aidanlaing.exoplanets.common.adapters.confirmedplanets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aidanlaing.exoplanets.R
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanet
import kotlinx.android.synthetic.main.item_confirmed_planet.view.*

class ConfirmedPlanetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(confirmedPlanet: ConfirmedPlanet) = with(itemView) {
        planetNameTv.text = confirmedPlanet.planetName
        hostStarNameTv.text = confirmedPlanet.hostStarName
    }

    companion object {
        fun inflate(parent: ViewGroup) = ConfirmedPlanetViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_confirmed_planet, parent, false)
        )
    }

}