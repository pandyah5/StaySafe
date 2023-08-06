package com.hp.staysafe.Location

import com.hp.staysafe.data.GlobalNeighbourhoodFatalityData
import com.hp.staysafe.data.GlobalNeighbourhoodLatLonData

data class LiveLocation (var latitude: String, var longitude: String) {
    var neighbourHood: String = "Unknown"
    var fatalityScore: Double = -1.0
    var safetyMessage: String = ""

    init {
        // Check if location is within Toronto
        val lat = latitude.toDouble()
        val lon = longitude.toDouble()
        if ((lat < 43.9 && lat > 43.4) && (lon < -79.1 && lon > -79.7)) {
            neighbourHood = GlobalNeighbourhoodLatLonData.setNeighbourhoodFromLatLon(lat, lon)
            fatalityScore = GlobalNeighbourhoodFatalityData.getFatalityScoreFromNeighbourhood(neighbourHood) * 10

            when (fatalityScore) {
                in 0.0..2.0 -> safetyMessage = "The data suggests very low safety risk, however, never let your guard down."
                in 2.0..4.0 -> safetyMessage = "The data suggests low safety risk, however, never let your guard down."
                in 4.0..6.0 -> safetyMessage = "The data suggests moderate safety risk, please be vigilant at all times."
                in 6.0..8.0 -> safetyMessage = "The data suggests high safety risk, please consider staying indoors."
                in 8.0..10.0 -> safetyMessage = "The data suggests high safety risk, please consider staying indoors."
            }
        }
        else {
            neighbourHood = "a location outside Toronto"
            safetyMessage = "Armour is only trained for locations within the city of Toronto, Canada."
        }
    }
}
