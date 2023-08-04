package com.hp.staysafe

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : ComponentActivity() {
    // Declare the location provider client
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the current date and time of the user
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDateTime = sdf.format(Date())
        var parseFailed = !parseDateTime(currentDateTime)
        if (parseFailed) {
            println(">>> ERROR: The date parsing failed!")
        }

        // Load and initialize the neighbourhood x, y coordinate data
        initializeNeighbourhoodLatLonData()

        // Load and initialize neighbourhood fatality score data
        initializeFatalityScoreData(todayDate.month)

        setContent {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button (onClick = {
                    val navigate = Intent(this@MainActivity, HomeScreen::class.java)
                    startActivity(navigate)
                }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                    Image(
                        painterResource(id = R.drawable.armourlogoshieldtransparent),
                        contentDescription = "Armour logo",
                        modifier = Modifier.size(200.dp)
                    )
                }

                Text (text = "Toronto Armour", fontFamily = FontFamily.Serif, fontSize = 25.sp)

                Spacer(modifier = Modifier.height(20.dp))

                Text (text = "Pat Armour to continue")

                Spacer(modifier = Modifier.height(150.dp))

                Text (text = "Hint: Armour is our four-legged friend!", fontFamily= FontFamily.Serif, fontSize = 12.sp)
            }
        }
    }

    private fun initializeNeighbourhoodLatLonData() {
        val bufferReader = BufferedReader(assets.open("neighbourhood_xy.csv").reader())
        val csvParser = CSVParser.parse(bufferReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase())

        var count: Int = 0
        csvParser.forEach {
            count++
            it?.let {
                val neighbourhoodEntry = neighbourhoodXY(it.get(0).toString(), it.get(2).toDouble(), it.get(1).toDouble())
                GlobalNeighbourhoodLatLonData.addNeighbourhood(neighbourhoodEntry)
            }
        }

        println(">>> SUCCESS: Added $count neighbourhood entries in entrees")
    }
    private fun initializeFatalityScoreData(month:Int) {
        var monthString = ""
        when (month) {
            1 -> monthString = "January"
            2 -> monthString = "February"
            3 -> monthString = "March"
            4 -> monthString = "April"
            5 -> monthString = "May"
            6 -> monthString = "June"
            7 -> monthString = "July"
            8 -> monthString = "August"
            9 -> monthString = "September"
            10 -> monthString = "October"
            11 -> monthString = "November"
            12 -> monthString = "December"
        }
        val bufferReader = BufferedReader(assets.open("$monthString.csv").reader())
        val csvParser = CSVParser.parse(bufferReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase())

        var count: Int = 0
        csvParser.forEach {
            count++
            it?.let {
                GlobalNeighbourhoodFatalityData.addNeighbourhoodFatality(it.get(0), it.get(1).toDouble())
            }
        }

        println(">>> INFO: We parsed $count rows in the $monthString.csv file")
        return
    }
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