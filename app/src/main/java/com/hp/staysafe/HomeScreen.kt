package com.hp.staysafe

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hp.staysafe.Location.LiveLocation
import com.hp.staysafe.Location.LocationService
import com.hp.staysafe.ui.theme.StaySafeTheme

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            ),
            0
        )

        setContent {
            StaySafeTheme {
                // Get an instance of the location viewModel to share the lat and lon coordinates
                val locationViewModel: LocationViewModel = viewModel<LocationViewModel>()
                val locationInfo by locationViewModel.getLocationLiveData().observeAsState()

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

                    HomeScreen(
                        applicationContext,
                        locationInfo,
                        "Safety Tip: Data suggests that morning is the safest time of the day, so don’t shy away from your morning walks!"
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(context: Context, locationInfo : LiveLocation?, safetyTip : String){
    val transparency = 0.5f

    Column (Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        ) {
        // An upper bar for app name and sponsor
        Surface(shadowElevation = 1.dp,
                color = Color.LightGray.copy(alpha = transparency)) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = transparency)),
                horizontalArrangement = Arrangement.SpaceBetween) {

                // Settings Button
                Button (onClick= {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
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
                Button (onClick= {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
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
        BoxWithConstraints {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                shadowElevation = 1.dp,
                color = Color.LightGray.copy(alpha = transparency),
                modifier = Modifier.padding(20.dp)
            )
            {
                Text(
                    text = "Hi I am Armour! If you hear a woof, its my way of alerting you!",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        top = 15.dp,
                        bottom = 15.dp,
                        start = 5.dp,
                        end = 5.dp
                    )
                )
            }
            Column () {
                Spacer(
                    modifier = Modifier
                        .height(50.dp)
                        .width(350.dp)
                )
                Image(
                    painterResource(id = R.drawable.armourstanding),
                    contentDescription = "Armour standing",
                    modifier = Modifier.size(100.dp)
                )
            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        // Message indicating the safety of current location
        Surface (shape = MaterialTheme.shapes.extraLarge,
            shadowElevation = 1.dp,
            color = Color.LightGray.copy(alpha = transparency),
            modifier = Modifier.padding(20.dp).align(CenterHorizontally)
        ) {
            Row (modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                if (locationInfo != null) {
                    Text (
                        modifier = Modifier.padding(all = 8.dp),
                        text = "It looks like you are currently in ${locationInfo.neighbourHood}. ${locationInfo.safetyMessage}",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Surface (shape = MaterialTheme.shapes.extraLarge,
            shadowElevation = 1.dp,
            color = Color.LightGray.copy(alpha = transparency),
            modifier = Modifier.padding(20.dp).align(CenterHorizontally)
        ) {
            var trackingLocation = remember {mutableStateOf(LocationService.ACTION_START)}
            Button(onClick = {
                Intent(context.applicationContext, LocationService::class.java).apply {
                    action = trackingLocation.value
                    context.applicationContext.startService(this)
                }

                if (trackingLocation.value == LocationService.ACTION_START){
                    trackingLocation.value = LocationService.ACTION_STOP
                }
                else {
                    trackingLocation.value = LocationService.ACTION_START
                }

            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent))
            {
                if (trackingLocation.value == LocationService.ACTION_START){
                    Text(text = "Start tracking location", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                }
                else {
                    Text(text = "Stop tracking location", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Tips: Safety tips for the user
        Surface (shape = MaterialTheme.shapes.extraLarge,
            shadowElevation = 1.dp,
            color = Color.LightGray.copy(alpha = transparency),
            modifier = Modifier.padding(20.dp).align(CenterHorizontally)
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "$safetyTip",
                    modifier = Modifier.padding(all = 5.dp),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}