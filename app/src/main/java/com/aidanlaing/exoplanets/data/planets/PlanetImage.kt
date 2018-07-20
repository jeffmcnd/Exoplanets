package com.aidanlaing.exoplanets.data.planets

import android.graphics.Color
import java.util.*

sealed class PlanetImage(val color: Int) {

    class GasOne(color: Int) : PlanetImage(color)
    class GasTwo(color: Int) : PlanetImage(color)
    class GasThree(color: Int) : PlanetImage(color)

    class RockyOne(color: Int) : PlanetImage(color)
    class RockyTwo(color: Int) : PlanetImage(color)
    class RockyThree(color: Int) : PlanetImage(color)

    class WaterLandOne(color: Int) : PlanetImage(color)
    class WaterLandTwo(color: Int) : PlanetImage(color)
    class WaterLandThree(color: Int) : PlanetImage(color)

    companion object {
        fun from(planet: Planet): PlanetImage {
            val seed = planet.planetName.hashCode().toLong()
            val random = Random(seed)

            val h = 0f + random.nextInt(360)
            val s = 42f + random.nextInt(98)
            val v = 40f + random.nextInt(90)
            val color = Color.HSVToColor(100, floatArrayOf(h, s, v))

            return when (random.nextInt(9)) {
                0 -> GasOne(color)
                1 -> GasTwo(color)
                2 -> GasThree(color)
                3 -> RockyOne(color)
                4 -> RockyTwo(color)
                5 -> RockyThree(color)
                6 -> WaterLandOne(color)
                7 -> WaterLandTwo(color)
                8 -> WaterLandThree(color)
                else -> GasOne(color)
            }
        }
    }

}