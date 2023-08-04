package com.hp.staysafe.data

import androidx.compose.runtime.mutableStateOf

data class LiveLocation (var latitude: String, var longitude: String) {
    var neighbourHood: String = "Toronto"
    var fatalityScore: Double = -1.0
    var safetyMessage: String = "Error in analyzing safety risk."

    init {
        neighbourHood = GlobalNeighbourhoodLatLonData.setNeighbourhoodFromLatLon(latitude.toDouble(), longitude.toDouble())
        fatalityScore = GlobalNeighbourhoodFatalityData.getFatalityScoreFromNeighbourhood(neighbourHood) * 10

        when (fatalityScore) {
            in 0.0..2.0 -> safetyMessage = "The data suggests very low safety risk, however, never let your guard down."
            in 2.0..4.0 -> safetyMessage = "The data suggests low safety risk, however, never let your guard down."
            in 4.0..6.0 -> safetyMessage = "The data suggests moderate safety risk, please be vigilant at all times."
            in 6.0..8.0 -> safetyMessage = "The data suggests high safety risk, please consider staying indoors."
            in 8.0..10.0 -> safetyMessage = "The data suggests high safety risk, please consider staying indoors."
        }
    }
}
