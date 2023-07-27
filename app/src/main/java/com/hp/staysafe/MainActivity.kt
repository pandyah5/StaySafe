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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hp.staysafe.ui.theme.StaySafeTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get live location of the user
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

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

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if (it != null){
                Toast.makeText(applicationContext, "${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()
                GPSLocation.setLat(it.latitude)
                GPSLocation.setLon(it.longitude)
                println("Latitude: ${it.latitude}")
                println("Longitude: ${it.longitude}")
            }
        }
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
                        modifier = Modifier.size(30.dp)
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
                Text (
                    modifier = Modifier.padding(all = 8.dp),
                    style = MaterialTheme.typography.titleLarge,
                    text = "${GPSLocation.getLat()}, ${GPSLocation.getLon()}"
                )

                Spacer(Modifier.weight(1f))

                Button (onClick= {}, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
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