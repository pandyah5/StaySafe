package com.hp.staysafe

data class LiveLocation (var latitude: String, var longitude: String) {
    var neighbourHood: String = ""

    init {
        neighbourHood = Global.setNeighbourhoodFromLatLon(latitude.toDouble(), longitude.toDouble())
    }
}
