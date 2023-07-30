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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    HomeScreen("It looks like you are currently near Mount Dennis. The data suggets low threat levels at this time.\n" +
                            "\n" +
                            "However, never keep your guard down!",
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
                .background(Color.LightGray),
                horizontalArrangement = Arrangement.SpaceBetween) {
                // Settings Button
                Button (onClick= {}, colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)) {
                    Image (
                        painterResource(id = R.drawable.setting_icon),
                        contentDescription ="Settings icon",
                        modifier = Modifier.size(20.dp)
                    )
                }

                // App Name
                Text (
                    text = "Toronto Armour",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(all = 8.dp)
                )

                // Sponsor Button
                Button (onClick= {}, colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)) {
                    Image (
                        painterResource(id = R.drawable.gift_icon),
                        contentDescription ="Sponsor heart icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // A rounded info message introducing 'Armour'
        Surface (shape = MaterialTheme.shapes.extraLarge,
            shadowElevation = 1.dp,
            color = Color.LightGray,
            modifier = Modifier.padding(15.dp)
        )
        {
            Text (
                text = "Hi I am Armour! If you hear a woof, its my way of alerting you!",
                fontFamily = FontFamily.Serif,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 5.dp, end = 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        // Message indicating the safety of current location
        Surface (shape = MaterialTheme.shapes.extraLarge,
            shadowElevation = 1.dp,
            color = Color.LightGray,
            modifier = Modifier.padding(10.dp).align(CenterHorizontally)
        ) {
            Row (modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Text (
                    modifier = Modifier.padding(all = 8.dp),
                    text = "$safetyAnalysis",
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif,
                )
            }
        }

        // A rounded text bar for current location
        Surface (shape = MaterialTheme.shapes.extraLarge,
            shadowElevation = 1.dp,
            color = Color.LightGray.copy(alpha = 0.6f),
            modifier = Modifier.padding(10.dp).align(CenterHorizontally)
        ){
            Row (horizontalArrangement = Arrangement.Center) {
                var latitude by remember { mutableStateOf("Latitude") }
                var longitude by remember { mutableStateOf("Longitude") }

                Button (onClick= {
                    latitude = GPSLocation.getLat().toBigDecimal().toPlainString();
                    longitude = GPSLocation.getLon().toBigDecimal().toPlainString()},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)) {
                    Text (
                        modifier = Modifier.padding(all = 5.dp),
                        color = Color.Black,
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp,
                        text = "Refresh location"
                    )
                }
            }
        }

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