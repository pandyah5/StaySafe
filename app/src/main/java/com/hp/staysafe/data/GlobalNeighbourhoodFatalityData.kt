package com.hp.staysafe.data

public class GlobalNeighbourhoodFatalityData {
    companion object {
        private var neighbourhoodFatalityList = hashMapOf<String, Double>()

        fun addNeighbourhoodFatality(hood: String, fatalityScore: Double) {
            neighbourhoodFatalityList[hood] = fatalityScore
        }

        fun getFatalityScoreFromNeighbourhood(hood: String) : Double {
            return neighbourhoodFatalityList[hood]?: 0.0
        }
    }

}