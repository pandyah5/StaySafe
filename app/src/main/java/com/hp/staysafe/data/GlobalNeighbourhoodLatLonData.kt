package com.hp.staysafe.data

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

public class GlobalNeighbourhoodLatLonData () {
    companion object {
        private var neighbourhoodList: MutableList<neighbourhoodXY> = mutableListOf()

        fun addNeighbourhood(entry: neighbourhoodXY) {
            neighbourhoodList.add(entry)
        }

        // Helper functions for finding current neighbourhood
        private fun deg2rad(deg: Double) : Double{
            return deg * (PI /180)
        }

        fun getDistanceFromLatLonInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double) : Double {
            val r = 6371; // Radius of the earth in km
            val dLat = deg2rad(lat2-lat1);  // deg2rad below
            val dLon = deg2rad(lon2-lon1);
            val a =
                sin(dLat/2) * sin(dLat/2) +
                        cos(deg2rad(lat1)) * cos(deg2rad(lat2)) *
                        sin(dLon/2) * sin(dLon/2)
            ;
            val c = 2 * atan2(sqrt(a), sqrt(1-a));
            val d = r * c; // Distance in km
            return d;
        }

        fun setNeighbourhoodFromLatLon(lat: Double, lon: Double) : String {
            println(">>> INFO: Searching neighbourhood near Lat: $lat, Lon: $lon")
            var neighbourhood: String = "Agincourt North"
            var minimumDistance: Double = 10000.0

            val iterator = neighbourhoodList.listIterator()
            for (item in iterator) {
                val neighbourhoodName = item.neighbourhood158Name
                val neighbourhoodLat = item.Lat
                val neighbourhoodLon = item.Lon

                var distanceFromUser = getDistanceFromLatLonInKm(lat, lon, neighbourhoodLat, neighbourhoodLon)
                // println("$neighbourhoodName: $distanceFromUser")

                if (distanceFromUser < minimumDistance) {
                    minimumDistance = distanceFromUser
                    neighbourhood = neighbourhoodName
                }
            }

            println(">>> INFO: User is in $neighbourhood")
            return neighbourhood
        }
    }
}

data class neighbourhoodXY (val name: String, val x: Double, val y: Double){
    var neighbourhood158Name: String = this.name
    var Lat: Double = this.x
    var Lon: Double = this.y
}