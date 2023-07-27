package com.hp.staysafe

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
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : ComponentActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get live location of the user
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val location_retrieved = fetchLocation()

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDateTime = sdf.format(Date())
        var parseFailed = !parseDateTime(currentDateTime)
        if (parseFailed) {
            println(">>> ERROR: The date parsing failed!")
        }

        setContent {
            StaySafeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen("Your current location is safe :)",
                              "Avoid travelling to Sherbourne and Jarvis right now",
                            "26th July, 2023")
                }
            }
        }
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

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen("Your current location is safe :)",
              "Avoid travelling to Sherbourne and Jarvis right now",
            "26th July, 2023")
}

@Composable
fun HomeScreen(safetyAnalysis: String,
               safetyTip: String,
               lastUpdated: String){
    println("Building Homescreen!")
    var lat by remember { mutableStateOf("Current latitude")}
    var lon by remember { mutableStateOf("Current longitude")}

    Column (Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween) {
        // An upper bar for app name and sponsor
        Surface(shadowElevation = 1.dp) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray),
                horizontalArrangement = Arrangement.End) {
                // App Name
                Text (
                    text = "Toronto Armour",
                    modifier = Modifier.padding(all = 8.dp),
                    style = MaterialTheme.typography.titleLarge
                )

                // Make the App Name left aligned
                Spacer(Modifier.weight(1f))

                // Sponsor Button
                Button (onClick= {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                    Image (
                        painterResource(id = R.drawable.heart_icon),
                        contentDescription ="Sponsor heart icon",
                        modifier = Modifier.size(30.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // A rounded text bar for current location with refresh icon
        Surface (shape = MaterialTheme.shapes.medium,
                 shadowElevation = 1.dp,
                 border = BorderStroke(2.dp,Color.Blue)
        ){
            Row (modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.End) {
                var latitude by remember { mutableStateOf("Latitude") }
                var longitude by remember { mutableStateOf("Longitude") }
                Text (
                    modifier = Modifier.padding(all = 8.dp),
                    style = MaterialTheme.typography.titleLarge,
                    text = "$latitude, $longitude"
                )

                Spacer(Modifier.weight(1f))

                Button (onClick= {latitude = GPSLocation.getLat().toBigDecimal().toPlainString();longitude = GPSLocation.getLon().toBigDecimal().toPlainString()},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                    Image (
                        painterResource(id = R.drawable.refresh),
                        contentDescription ="Refresh icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        // Message indicating the safety of current location
        Row (modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()) {
            Text(
                text = "$safetyAnalysis",
                modifier = Modifier.padding(all = 8.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }

        // Spacer(modifier = Modifier.height(30.dp))

        // Tips: Which location too avoid at given time
        Row (modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()) {
            Text(
                text = "$safetyTip",
                modifier = Modifier.padding(all = 8.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }

        // Disclaimer
        Row (modifier = Modifier
            .weight(1f, false)
            .padding(20.dp)
            .fillMaxWidth()) {
            Text(
                text = "The information above is based on the official data provided by Toronto Police Service Public Safety Data Portal as of $lastUpdated",
                modifier = Modifier.padding(all = 8.dp),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}