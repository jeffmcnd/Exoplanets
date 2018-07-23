package com.aidanlaing.exoplanets.data.planets

import android.graphics.Color
import android.os.Parcelable
import com.aidanlaing.exoplanets.R
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PlanetImage(val color: Int, val resId: Int) : Parcelable {

    companion object {
        fun from(planet: Planet): PlanetImage {
            val seed = planet.name.hashCode().toLong()
            val random = Random(seed)

            val h = 0f + random.nextInt(360)
            val s = 42f + random.nextInt(98)
            val v = 40f + random.nextInt(90)
            val color = Color.HSVToColor(100, floatArrayOf(h, s, v))

            val drawable = drawables[random.nextInt(drawables.size)]

            return PlanetImage(color, drawable)
        }

        private val drawables = arrayListOf(
                R.drawable.ic_planet_1,
                R.drawable.ic_planet_2,
                R.drawable.ic_planet_3,
                R.drawable.ic_planet_4,
                R.drawable.ic_planet_5,
                R.drawable.ic_planet_6
        )
    }

}