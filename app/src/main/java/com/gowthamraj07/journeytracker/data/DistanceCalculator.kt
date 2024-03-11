package com.gowthamraj07.journeytracker.data

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * This class is used to calculate the distance between two points on the Earth's surface using Haversine formula.
 */
class DistanceCalculator {

    private val radiusOfEarthInMeters = 6371e3 // Earth radius in meters

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radiusOfEarthInMeters * c // Distance in meters
    }

    fun isDistanceOver100Meters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Boolean =
        calculateDistance(lat1, lon1, lat2, lon2) > 100
}
