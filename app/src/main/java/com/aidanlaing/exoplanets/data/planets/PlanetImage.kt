package com.aidanlaing.exoplanets.data.planets

import android.graphics.Color
import java.util.*

sealed class PlanetImage(val color: Int) {

    class Stripe(color: Int) : PlanetImage(color)
    class Crevice(color: Int) : PlanetImage(color)
    class WaterLand(color: Int) : PlanetImage(color)

    companion object {
        fun from(planet: Planet): PlanetImage {
            val seed = planet.planetName.hashCode().toLong()
            val random = Random(seed)

            val h = 0f + random.nextInt(360)
            val s = 42f + random.nextInt(98)
            val v = 40f + random.nextInt(90)
            val color = Color.HSVToColor(100, floatArrayOf(h, s, v))

            return when (random.nextInt(3)) {
                0 -> Stripe(color)
                1 -> Crevice(color)
                2 -> WaterLand(color)
                else -> WaterLand(color)
            }
        }
    }

}