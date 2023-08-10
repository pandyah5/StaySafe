package com.hp.staysafe.Sponsor

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hp.staysafe.HomeScreen
import com.hp.staysafe.Location.LocationService
import com.hp.staysafe.Location.LocationViewModel
import com.hp.staysafe.R
import com.hp.staysafe.Settings.SettingActivity
import com.hp.staysafe.ui.theme.BabyBlue
import com.hp.staysafe.ui.theme.BlueGrotto
import com.hp.staysafe.ui.theme.NavyBlue
import com.hp.staysafe.ui.theme.StaySafeTheme

class SponsorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StaySafeTheme {
                // URL Handler to open GitHub repo
                val uriHandler = LocalUriHandler.current

                // A box container to set the app background color
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BabyBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        // An upper bar for app name and sponsor
                        Surface(
                            shadowElevation = 1.dp,
                            color = BabyBlue
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(BabyBlue),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                // Armour's Icon (Always redirects to Armour's Screen)
                                Button(
                                    onClick = {
                                        val navigate = Intent(
                                            this@SponsorActivity,
                                            SettingActivity::class.java
                                        )
                                        navigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        this@SponsorActivity.startActivity(navigate)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BabyBlue)
                                ) {
                                    Image(
                                        painterResource(id = R.drawable.dog_icon),
                                        contentDescription = "Armour's icon",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                // App Name (Always redirects to HomeScreen)
                                Button(onClick = {
                                    val navigate =
                                        Intent(this@SponsorActivity, HomeScreen::class.java)
                                    navigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    this@SponsorActivity.startActivity(navigate)
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
                                Button(
                                    onClick = {
                                        val navigate = Intent(
                                            this@SponsorActivity,
                                            SponsorActivity::class.java
                                        )
                                        navigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        this@SponsorActivity.startActivity(navigate)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BabyBlue)
                                ) {
                                    Image(
                                        painterResource(id = R.drawable.gift_icon),
                                        contentDescription = "Sponsor gift icon",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        // Message indicating the safety of current location
                        BoxWithConstraints {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                shadowElevation = 1.dp,
                                color = BlueGrotto,
                                modifier = Modifier
                                    .padding(0.dp)
                                    .clip(shape = RoundedCornerShape(0.dp, 0.dp, 60.dp, 60.dp))
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        start = 35.dp,
                                        top = 15.dp,
                                        end = 35.dp,
                                        bottom = 35.dp
                                    ),
                                    text = "Hi! Armour here. Thank you for considering to support HP, the creator of this app.\n" +
                                            "\nThis is a free and open-source initiative and it will remain so." +
                                            "If you wish to help HP improve this app please consider following him and staring his project on GitHub",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                            }
                            Column() {
                                Spacer(
                                    modifier = Modifier
                                        .height(260.dp)
                                        .width(350.dp)
                                )
                                Image(
                                    painterResource(id = R.drawable.armourstanding),
                                    contentDescription = "Armour standing",
                                    modifier = Modifier.size(100.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(70.dp))

                        Surface (shape = MaterialTheme.shapes.extraLarge,
                            shadowElevation = 20.dp,
                            color = NavyBlue,
                            modifier = Modifier
                                .padding(20.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Button(onClick = {
                                // To add link to the exact GitHub repo
                                uriHandler.openUri("https://github.com/pandyah5")
                            }, colors = ButtonDefaults.buttonColors(containerColor = NavyBlue))
                            {
                                Text(text = "Source code on GitHub", style = MaterialTheme.typography.titleMedium, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Surface(
                            shape = MaterialTheme.shapes.small,
                            shadowElevation = 1.dp,
                            color = BlueGrotto,
                            modifier = Modifier
                                .padding(0.dp)
                                .clip(shape = RoundedCornerShape(60.dp, 60.dp, 0.dp, 0.dp))
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                    start = 35.dp,
                                    top = 15.dp,
                                    end = 35.dp,
                                    bottom = 15.dp
                                ),
                                text = "HP does not expect any monetary compensation.\n\n" +
                                        " However, if you really want to support him monetarily you can do so by sponsoring him on GitHub!",
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}