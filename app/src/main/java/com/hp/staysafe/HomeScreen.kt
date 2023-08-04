package com.hp.staysafe

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hp.staysafe.data.LiveLocation
import com.hp.staysafe.ui.theme.StaySafeTheme

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StaySafeTheme {
                // Get an instance of the location viewModel to share the lat and lon coordinates
                val locationViewModel: LocationViewModel = viewModel<LocationViewModel>()
                val location by locationViewModel.getLocationLiveData().observeAsState()

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
                        location,
                        "Safety Tip: Data suggests that morning is the safest time of the day, so don’t shy away from your morning walks!"
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(location : LiveLocation?, safetyTip : String){
    println(">>> INFO: Drafting the safety risk message")
    var safetyMessage = "TODO"
//    if (Global.fatalityScore <= 0.2) {
//        safetyMessage = "The data suggests very low safety risk, however, never let your guard down."
//    }
//    else if (0.2 < Global.fatalityScore && Global.fatalityScore <= 0.4) {
//        safetyMessage = "The data suggests low safety risk, however, never let your guard down."
//    }
//    else if (0.4 < Global.fatalityScore && Global.fatalityScore <= 0.6) {
//        safetyMessage = "The data suggests moderate safety risk, please be vigilant at all times."
//    }
//    else if (0.6 < Global.fatalityScore && Global.fatalityScore <= 0.8) {
//        safetyMessage = "The data suggests high safety risk, please consider staying indoors."
//    }
//    else if (0.8 < Global.fatalityScore && Global.fatalityScore <= 1) {
//        safetyMessage = "The data suggests high safety risk, please consider staying indoors."
//    }
//    else {
//        safetyMessage = "Error in analyzing safety risk."
//        println(">>> ERROR: Fatality score is not in the expected range. Please debug the error")
//    }

    println(">>> INFO: Building Homescreen!")
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
                    style = MaterialTheme.typography.titleSmall,
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
                if (location != null) {
                    Text (
                        modifier = Modifier.padding(all = 8.dp),
                        text = "It looks like you are currently in ${location.neighbourHood} and the fatality score is ${location.fatalityScore}",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // A refresh button
        Surface (shape = MaterialTheme.shapes.extraLarge,
            shadowElevation = 1.dp,
            color = Color.White,
            modifier = Modifier.padding(20.dp).align(CenterHorizontally)
        ){
            Row (horizontalArrangement = Arrangement.Center) {
                if (location != null) {
                    Text (
                        modifier = Modifier.padding(all = 5.dp),
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Black,
                        text = "Live location ${location.latitude}, ${location.longitude}"
                    )
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
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}