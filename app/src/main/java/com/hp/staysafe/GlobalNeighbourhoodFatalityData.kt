package com.hp.staysafe

public class GlobalNeighbourhoodFatalityData {
    companion object {
        private var neighbourhoodFatalityList = hashMapOf<String, Double>()

        fun addNeighbourhoodFatality(hood: String, fatalityScore: Double) {
            GlobalNeighbourhoodFatalityData.neighbourhoodFatalityList[hood] = fatalityScore
        }

        fun getFatalityScoreFromNeighbourhood(hood: String) : Double {
            return neighbourhoodFatalityList[hood]?: 0.0
        }
    }

}