package com.aidanlaing.exoplanets.common.adapters.confirmedplanets

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanet

class ConfirmedPlanetsAdapter(
        private val confirmedPlanetClickListener: (confirmedPlanet: ConfirmedPlanet) -> Unit,
        private var confirmedPlanets: ArrayList<ConfirmedPlanet> = ArrayList()
): RecyclerView.Adapter<ConfirmedPlanetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmedPlanetViewHolder =
            ConfirmedPlanetViewHolder.inflate(parent)

    override fun getItemCount(): Int = confirmedPlanets.size

    override fun onBindViewHolder(holder: ConfirmedPlanetViewHolder, position: Int) {
        holder.bind(confirmedPlanets[position], confirmedPlanetClickListener)
    }

    fun replaceItems(confirmedPlanets: ArrayList<ConfirmedPlanet>) {
        this.confirmedPlanets = confirmedPlanets
        notifyDataSetChanged()
    }
}