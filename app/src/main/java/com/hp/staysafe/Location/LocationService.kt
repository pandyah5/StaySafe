package com.hp.staysafe.Location

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.hp.staysafe.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class LocationService: Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    lateinit var locationInfo : LocationLiveData
    private var lastNeighborhood = ""

    private val observer = Observer<LiveLocation> { data ->
        // Only alert the user if they have entered into a new high safety risk hood
        // This prevents continuous spamming notifs when user stays in a high fatality hood for longer time
        var userChangedNeighborhood: Boolean = true
        if (lastNeighborhood == data.neighbourHood) {
            userChangedNeighborhood = false
        }
        else {
            lastNeighborhood = data.neighbourHood
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (data.fatalityScore >= 8 && userChangedNeighborhood) {
            val notification = NotificationCompat.Builder(this, "safetyAlert")
                .setContentTitle("ALERT: There is high safety risk in this neighbourhood")
                .setContentText("Current location: ${locationInfo.value?.neighbourHood}")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)

            notificationManager.notify(1, notification.build())
        }
        else {
            val notification = NotificationCompat.Builder(this, "locationUpdates")
                .setContentTitle("Scanning your neighbourhood for safety...")
                .setContentText("Current location: ${locationInfo.value?.neighbourHood}")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)

            notificationManager.notify(1, notification.build())
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationInfo = LocationLiveData(this.application)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        println(">>> INFO: Notification service is started")
        val notification = NotificationCompat.Builder(this, "locationUpdates")
            .setContentTitle("Scanning your neighbourhood for safety...")
            .setContentText("Current location: ${locationInfo.value?.neighbourHood}")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        locationInfo.observeForever(observer)
        locationInfo.startLocationUpdates()

        startForeground(1, notification.build())
    }

    private fun stop() {
        println(">>> INFO: Notification service is stopped")
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