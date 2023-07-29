package com.hp.staysafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hp.staysafe.ui.theme.StaySafeTheme

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StaySafeTheme {
                // A box container to set the app background
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.toronto_bg),
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = "Background Image",
                        contentScale = ContentScale.Crop,
                        alpha = 0.4F
                    )
                    HomeScreen("Your current location is safe :)",
                        "Avoid travelling to Sherbourne and Jarvis right now",
                        "26th July, 2023")
                }
            }
        }
    }
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

    Column (Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        ) {
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
            border = BorderStroke(2.dp, Color.Blue)
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

                Button (onClick= {
                    latitude = GPSLocation.getLat().toBigDecimal().toPlainString();
                    longitude = GPSLocation.getLon().toBigDecimal().toPlainString()},
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