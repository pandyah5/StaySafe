package com.hp.staysafe.Armour

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hp.staysafe.HomeScreen
import com.hp.staysafe.R
import com.hp.staysafe.Sponsor.SponsorActivity
import com.hp.staysafe.ui.theme.BabyBlue
import com.hp.staysafe.ui.theme.BlueGrotto
import com.hp.staysafe.ui.theme.StaySafeTheme

class ArmourActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StaySafeTheme {
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
                                            this@ArmourActivity,
                                            ArmourActivity::class.java
                                        )
                                        navigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        this@ArmourActivity.startActivity(navigate)
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
                                        Intent(this@ArmourActivity, HomeScreen::class.java)
                                    navigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    this@ArmourActivity.startActivity(navigate)
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
                                            this@ArmourActivity,
                                            SponsorActivity::class.java
                                        )
                                        navigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        this@ArmourActivity.startActivity(navigate)
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

                        // Text giving a brief intro about Armour
                        BoxWithConstraints {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                shadowElevation = 1.dp,
                                color = BlueGrotto,
                                modifier = Modifier
                                    .padding(0.dp)
                                    .clip(
                                        shape = RoundedCornerShape(
                                            0.dp,
                                            0.dp,
                                            60.dp,
                                            60.dp
                                        )
                                    )
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        start = 35.dp,
                                        top = 15.dp,
                                        end = 35.dp,
                                        bottom = 35.dp
                                    ),
                                    text = "Hi! My name is Armour. I am a German Shepherd trained on the data supplied by Toronto Police Service’s Public Safety Data Portal.\n" +
                                            "\n" +
                                            "This app was built by my friend HP who loves exploring the city of Toronto by himself. ",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                            }
                            Column() {
                                Spacer(
                                    modifier = Modifier
                                        .height(220.dp)
                                        .width(350.dp)
                                )
                                Image(
                                    painterResource(id = R.drawable.armourstanding),
                                    contentDescription = "Armour standing",
                                    modifier = Modifier.size(100.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(50.dp))

                        Surface(
                            shape = MaterialTheme.shapes.extraLarge,
                            shadowElevation = 1.dp,
                            color = BlueGrotto,
                            modifier = Modifier.padding(20.dp)
                        )
                        {
                            Text(
                                text = "But HP gets really concerned when he hears news about shootings in Toronto. \n" +
                                        "\n" +
                                        "He wanted to raise a German Shepherd (like me) who could make him feel safe.\n",
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

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = MaterialTheme.shapes.extraLarge,
                            shadowElevation = 1.dp,
                            color = BlueGrotto,
                            modifier = Modifier.padding(20.dp)
                        ){
                            Text(
                                text = "Unfortunately, he is still a student and can barely take care of himself. So rather he made this app!",
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
                    }
                }
            }
        }
    }
}