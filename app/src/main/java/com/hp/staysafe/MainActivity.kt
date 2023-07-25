package com.hp.staysafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StaySafeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen("Bay and College St")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen("Bay and College St")
}

@Composable
fun HomeScreen(userLoc : String){
    Column {
        // An upper bar for app name and sponsor
        Surface(shadowElevation = 1.dp) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray),
                horizontalArrangement = Arrangement.End) {
                // App Name
                Text (
                    text = "App Name",
                    modifier = Modifier.padding(all = 8.dp),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.weight(1f))

                // Sponsor Button
                Button (onClick= {}) {
                    Image (
                        painterResource(id = R.drawable.heart_icon),
                        contentDescription ="Sponsor heart icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // A rounded text bar for current location with refresh icon
        Surface (shape = MaterialTheme.shapes.medium, shadowElevation = 1.dp){
            Row (modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.End) {
                Text (
                    modifier = Modifier.padding(all = 8.dp),
                    style = MaterialTheme.typography.titleLarge,
                    text = "$userLoc"
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

        // Message indicating the safety of current location

        // Tips: Which location too avoid at given time

        // Call 911 button at the bottom of the screen
    }
}