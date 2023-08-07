package com.hp.staysafe.Location

import android.Manifest
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.hp.staysafe.LocationViewModel
import com.hp.staysafe.R
import com.hp.staysafe.data.LocationLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService: Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    // private var serviceLocation = LiveLocation("1.0", "1.0")
    lateinit var locationInfo : LocationLiveData

    private val observer = Observer<LiveLocation> { data ->
        // Live data value has changed
        println("Location Service received coordinates ${data.latitude}, ${data.longitude}")
        // serviceLocation = data.let { LiveLocation(it.latitude, it.longitude) }

        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: ${locationInfo.value?.latitude}, ${locationInfo.value?.longitude}")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        println(">>> INFO: Created service")
        locationInfo = LocationLiveData(this.application)
        println(">>> INFO: Initialized locationInfo")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        println(">>> INFO: Creating notification!")
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: ${locationInfo.value?.latitude}, ${locationInfo.value?.longitude}")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        println(">>> INFO: Creating link to location live data")
        locationInfo.observeForever(observer)
        locationInfo.startLocationUpdates()

        startForeground(1, notification.build())
    }

    private fun stop() {
        println("Stopping service")
        locationInfo.removeObserver(observer)
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}