package com.hp.staysafe

data class LiveLocation (var latitude: String, var longitude: String) {
    var neighbourHood: String = ""
    var fatalityScore: Double = -1.0

    init {
        neighbourHood = GlobalNeighbourhoodLatLonData.setNeighbourhoodFromLatLon(latitude.toDouble(), longitude.toDouble())
        fatalityScore = GlobalNeighbourhoodFatalityData.getFatalityScoreFromNeighbourhood(neighbourHood)
    }
}
