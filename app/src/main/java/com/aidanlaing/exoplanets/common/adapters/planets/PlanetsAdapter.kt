package com.aidanlaing.exoplanets.common.adapters.planets

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import com.aidanlaing.exoplanets.data.planets.Planet

class PlanetsAdapter(
        private val planetClickListener: (
                planet: Planet,
                planetImageIv: ImageView
        ) -> Unit,
        private var planets: ArrayList<Planet> = ArrayList()
) : RecyclerView.Adapter<PlanetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetViewHolder =
            PlanetViewHolder.inflate(parent)

    override fun getItemCount(): Int = planets.size

    override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
        holder.bind(planets[position], planetClickListener)
    }

    fun replaceItems(planets: ArrayList<Planet>) {
        this.planets = planets
        notifyDataSetChanged()
    }
}