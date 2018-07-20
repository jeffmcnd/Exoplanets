package com.aidanlaing.exoplanets.data.planets

import android.graphics.Color
import java.util.*

sealed class PlanetImage(val color: Int) {

    class PlanetOne(color: Int) : PlanetImage(color)
    class PlanetTwo(color: Int) : PlanetImage(color)
    class PlanetThree(color: Int) : PlanetImage(color)
    class PlanetFour(color: Int) : PlanetImage(color)
    class PlanetFive(color: Int) : PlanetImage(color)
    class PlanetSix(color: Int) : PlanetImage(color)

    companion object {
        fun from(planet: Planet): PlanetImage {
            val seed = planet.planetName.hashCode().toLong()
            val random = Random(seed)

            val h = 0f + random.nextInt(360)
            val s = 42f + random.nextInt(98)
            val v = 40f + random.nextInt(90)
            val color = Color.HSVToColor(100, floatArrayOf(h, s, v))

            return when (random.nextInt(7)) {
                1 -> PlanetOne(color)
                2 -> PlanetTwo(color)
                3 -> PlanetThree(color)
                4 -> PlanetFour(color)
                5 -> PlanetFive(color)
                6 -> PlanetSix(color)
                else -> PlanetOne(color)
            }
        }
    }

}