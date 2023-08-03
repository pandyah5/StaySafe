package com.hp.staysafe

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hp.staysafe.ui.theme.StaySafeTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

public class Global {
    companion object {
        @JvmField
        var neighbourhoodFatalityList = hashMapOf<String, Double>()
        var entrees: MutableList<neighbourhoodXY> = mutableListOf()
        var currentNeighbourhood: String = "defaultNeighbourhood"
        var fatalityScore: Double = -1.0

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

        fun setNeighbourhoodFromLatLon(lat: Double, lon: Double) {
            println(">>> INFO: Searching neighbourhood near Lat: $lat, Lon: $lon")
            var neighbourhood: String = "Agincourt North"
            var minimumDistance: Double = 10000.0

            val iterator = Global.entrees.listIterator()
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
            currentNeighbourhood = neighbourhood
        }
    }
}

class MainActivity : ComponentActivity() {
    // Declare the location provider client
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get live location of the user
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRetrieved = fetchLocation()
        if (!locationRetrieved) {
            println(">>> ERROR: Failed to retrieve current location in main activity")
        }

        // Get the current date and time of the user
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDateTime = sdf.format(Date())
        var parseFailed = !parseDateTime(currentDateTime)
        if (parseFailed) {
            println(">>> ERROR: The date parsing failed!")
        }

        // Load the neighbourhood x, y coordinate data
        readNeighbourhoodXY()

        setContent {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button (onClick = {
                    // Get neighbourhood from GPS coordinates (lat, lon)
                    Global.setNeighbourhoodFromLatLon(GPSLocation.getLat(), GPSLocation.getLon())

                    // Get fatality score from csv data
                    Global.fatalityScore = getFatalityScore(todayDate.month, Global.currentNeighbourhood)
                    if (Global.fatalityScore == -1.0) {
                        println(">>> ERROR: Could not retrieve fatality score for ${Global.currentNeighbourhood} for ${todayDate.month}")
                    }
                    else {
                        println(">>> SUCCESS: The fatality score for ${Global.currentNeighbourhood} in ${todayDate.month} is ${Global.fatalityScore}")
                    }

                    val navigate = Intent(this@MainActivity, HomeScreen::class.java)
                    startActivity(navigate)
                }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                    Image(
                        painterResource(id = R.drawable.armourlogoshieldtransparent),
                        contentDescription = "Armour logo",
                        modifier = Modifier.size(200.dp)
                    )
                }

                Text (text = "Toronto Armour", fontFamily = FontFamily.Serif, fontSize = 25.sp)

                Spacer(modifier = Modifier.height(20.dp))

                Text (text = "Pat Armour to continue")

                Spacer(modifier = Modifier.height(150.dp))

                Text (text = "Hint: Armour is our four-legged friend!", fontFamily= FontFamily.Serif, fontSize = 12.sp)
            }
        }
    }

    private fun getFatalityScore(month:Int, hood:String) :Double{
        var monthString = ""
        when (month) {
            1 -> monthString = "January"
            2 -> monthString = "February"
            3 -> monthString = "March"
            4 -> monthString = "April"
            5 -> monthString = "May"
            6 -> monthString = "June"
            7 -> monthString = "July"
            8 -> monthString = "August"
            9 -> monthString = "September"
            10 -> monthString = "October"
            11 -> monthString = "November"
            12 -> monthString = "December"
        }
        val bufferReader = BufferedReader(assets.open("$monthString.csv").reader())
        val csvParser = CSVParser.parse(bufferReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase())

        var fatalityScore : Double = -1.0

        var count: Int = 0
        csvParser.forEach {
            count++
            it?.let {
                if (it.get(0) == hood) {
                    fatalityScore = it.get(1).toDouble()
                }
                Global.neighbourhoodFatalityList.put(it.get(0), it.get(1).toDouble())
            }
        }

        println(">>> INFO: We parsed $count rows in the $monthString.csv file")
        return fatalityScore
    }

    private fun readNeighbourhoodXY() {
        val bufferReader = BufferedReader(assets.open("neighbourhood_xy.csv").reader())
        val csvParser = CSVParser.parse(bufferReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase())

        var count: Int = 0
        csvParser.forEach {
            count++
            it?.let {
                val neighbourhoodEntry = neighbourhoodXY(it.get(0).toString(), it.get(2).toDouble(), it.get(1).toDouble())
                Global.entrees.add(neighbourhoodEntry)
            }
        }

        println(">>> SUCCESS: Added $count neighbourhood entries in entrees")
    }

    private fun fetchLocation(): Boolean {
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            println(">>> ERROR: Failed to fetch location!")
            return false
        }
        task.addOnSuccessListener {
            if (it != null){
                Toast.makeText(applicationContext, "${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()
                GPSLocation.setLat(it.latitude)
                GPSLocation.setLon(it.longitude)
                println(">>> SUCCESS: Recorded the current location")
            }
        }
        return true
    }
}

data class neighbourhoodFatality(val name: String, val score: Double) {
    var neighbourhood158Name = this.name
    var fatalityScore = this.score
}

data class neighbourhoodXY (val name: String, val x: Double, val y: Double){
    var neighbourhood158Name: String = this.name
    var Lat: Double = this.x
    var Lon: Double = this.y
}

// Static object to store the users live GPS coordinates
object GPSLocation {
    private var latitude: Double = 1.0
    private var longitude: Double = -1.0

    // Get functions
    fun getLat() :Double { return latitude }
    fun getLon() :Double { return longitude }

    // Set functions
    fun setLat(Lat: Double) { latitude = Lat }
    fun setLon(Lon: Double) { longitude = Lon }
}

object todayDate {
    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var min = 0
    var sec = 0
}

// Returns false if it is unable to parse datetime otherwise stores info in todayDate
fun parseDateTime(datetime: String) : Boolean{
    val dateAndTime = datetime.split(" ")
    if (dateAndTime.size != 2){ return false }
    var date = dateAndTime[0].split('/')
    var time = dateAndTime[1].split(':')

    // Store date
    todayDate.day = date[0].toInt()
    todayDate.month = date[1].toInt()
    todayDate.year = date[2].toInt()

    // Store time
    todayDate.hour = time[0].toInt()
    todayDate.min = time[1].toInt()
    todayDate.sec = time[2].toInt()

    return true
}