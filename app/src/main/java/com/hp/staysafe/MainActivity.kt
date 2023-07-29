package com.hp.staysafe

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : ComponentActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var fatalityScore: Double = -1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get live location of the user
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val location_retrieved = fetchLocation()

        // Get the current date and time of the user
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDateTime = sdf.format(Date())
        var parseFailed = !parseDateTime(currentDateTime)
        if (parseFailed) {
            println(">>> ERROR: The date parsing failed!")
        }

        // Get neighbourhood from GPS coordinates (lat, lon)
        var hood : String = "Agincourt North"

        // Get fatality score from csv data
        var fatalityScore = getFatalityScore(todayDate.month, hood)
        if (fatalityScore == -1.0) {
            println(">>> ERROR: Could not retrieve fatality score for $hood for ${todayDate.month}")
        }
        else {
            println(">>> SUCCESS: The fatality score for $hood in ${todayDate.month} is $fatalityScore")
        }

        setContent {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button (onClick = {
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
        run breaking@{
            csvParser.forEach {
                count++
                it?.let {
                    if (it.get(0) == hood) {
                        fatalityScore = it.get(1).toDouble()
                        return@breaking
                    }
                }
            }
        }

        println(">>> INFO: We parsed $count rows in the CSV file")

        return fatalityScore
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
            }
        }
        return true
    }
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