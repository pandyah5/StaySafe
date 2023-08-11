package com.hp.staysafe

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hp.staysafe.Armour.ArmourActivity
import com.hp.staysafe.Location.LiveLocation
import com.hp.staysafe.Location.LocationService
import com.hp.staysafe.Location.LocationViewModel
import com.hp.staysafe.Sponsor.SponsorActivity
import com.hp.staysafe.data.safetyTipData
import com.hp.staysafe.dataStore.DataStoreManager
import com.hp.staysafe.dataStore.LocStatusViewModel
import com.hp.staysafe.ui.theme.BabyBlue
import com.hp.staysafe.ui.theme.BlueGrotto
import com.hp.staysafe.ui.theme.NavyBlue
import com.hp.staysafe.ui.theme.StaySafeTheme

class HomeScreen : ComponentActivity() {
    private lateinit var dataStoreManager: DataStoreManager

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.FOREGROUND_SERVICE_LOCATION
            ),
            0
        )

        // Stores whether the location service is running in the background
        dataStoreManager = DataStoreManager(this, "locationStatus")

        setContent {
            StaySafeTheme {
                // Get an instance of the location viewModel to share the lat and lon coordinates
                val locationViewModel: LocationViewModel = viewModel<LocationViewModel>()
                val locationInfo by locationViewModel.getLocationLiveData().observeAsState()

                // A box container to set the app background color
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BabyBlue),
                    contentAlignment = Alignment.Center
                ) {
                    HomeScreen(
                        applicationContext,
                        locationInfo,
                        safetyTipData.getSafetyTip()
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(context: Context,locationInfo : LiveLocation?, safetyTip : String){
    Column (Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
        // An upper bar for app name and sponsor
        Surface(shadowElevation = 1.dp,
                color = BabyBlue) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .background(BabyBlue),
                horizontalArrangement = Arrangement.SpaceBetween) {

                // Armour's Icon (Always redirects to Armour's Screen)
                Button (onClick= {
                    val navigate = Intent(context, ArmourActivity::class.java)
                    navigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(navigate)
                }, colors = ButtonDefaults.buttonColors(containerColor = BabyBlue)) {
                    Image (
                        painterResource(id = R.drawable.dog_icon),
                        contentDescription = "Armour's icon",
                        modifier = Modifier.size(20.dp)
                    )
                }

                // App Name (Always redirects to HomeScreen)
                Button (onClick= {
                    val navigate = Intent(context, HomeScreen::class.java)
                    navigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(navigate)
                }, colors = ButtonDefaults.buttonColors(containerColor = BabyBlue))
                {
                        Text(
                            text = "Toronto Armour",
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = Color.Black
                        )
                }

                // Sponsor Icon (Always redirects to Sponsor Screen)
                Button (onClick= {
                    val navigate = Intent(context, SponsorActivity::class.java)
                    navigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(navigate)
                }, colors = ButtonDefaults.buttonColors(containerColor = BabyBlue)) {
                    Image (
                        painterResource(id = R.drawable.gift_icon),
                        contentDescription ="Sponsor gift icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Message indicating the safety of current location
        Surface (shape = MaterialTheme.shapes.small,
            shadowElevation = 1.dp,
            color = BlueGrotto,
            modifier = Modifier
                .padding(0.dp)
                .align(CenterHorizontally)
                .clip(shape = RoundedCornerShape(0.dp, 0.dp, 60.dp, 60.dp))
        ) {
            Row (modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                if (locationInfo != null) {
                    Text (
                        modifier = Modifier.padding(start = 8.dp, top = 15.dp, end = 8.dp, bottom = 35.dp),
                        text = "It looks like you are currently in ${locationInfo.neighbourHood}. \n\n ${locationInfo.safetyMessage}",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(150.dp))

        Surface (shape = MaterialTheme.shapes.extraLarge,
            shadowElevation = 20.dp,
            color = NavyBlue,
            modifier = Modifier
                .padding(20.dp)
                .align(CenterHorizontally)
        ) {
            val locStatusViewModel = viewModel<LocStatusViewModel>()
            val locationTrackingStatus = locStatusViewModel.getStatus.observeAsState()

            Button(onClick = {
                println(">>> INFO: The current loc status: ${locationTrackingStatus.value}")
                var actionToPerform = LocationService.ACTION_START
                if (locationTrackingStatus.value == "ACTIVE") {
                    actionToPerform = LocationService.ACTION_STOP
                }

                Intent(context.applicationContext, LocationService::class.java).apply {
                    action = actionToPerform
                    context.applicationContext.startService(this)
                }

                if (locationTrackingStatus.value == "ACTIVE"){
                    locStatusViewModel.setStatus("INACTIVE")
                }
                else {
                    locStatusViewModel.setStatus("ACTIVE")
                }

            }, colors = ButtonDefaults.buttonColors(containerColor = NavyBlue))
            {
                if (locationTrackingStatus.value == "ACTIVE"){
                    Text(text = "Stop tracking location", style = MaterialTheme.typography.titleMedium, color = Color.White)
                }
                else {
                    Text(text = "Start tracking location", style = MaterialTheme.typography.titleMedium, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))

        // Tips: Safety tips for the user
        BoxWithConstraints {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                shadowElevation = 1.dp,
                color = BlueGrotto,
                modifier = Modifier.padding(20.dp)
            )
            {
                Text(
                    text = "$safetyTip",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.padding(
                        top = 15.dp,
                        bottom = 15.dp,
                        start = 10.dp,
                        end = 10.dp
                    )
                )
            }
            Column {
                Spacer(
                    modifier = Modifier
                        .height(100.dp)
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
    }
}