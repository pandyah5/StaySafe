package com.hp.staysafe.Location
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.pm.PackageManager
//import android.location.Location
//import android.location.LocationManager
//import android.os.Looper
//import androidx.core.content.ContextCompat
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.launch
//
//class DefaultLocationClient(
//    private val context: Context,
//    private val client: FusedLocationProviderClient
//): LocationClient {
//    @SuppressLint("MissingPermission")
//    override fun getLocationUpdates(interval: Long): Flow<Location> {
//        return callbackFlow {
//            println(">>> Info: Reached callback function")
//            if(ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED) {
//                throw LocationClient.LocationException("Missing location permission")
//            }
//
//            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//            if(!isGpsEnabled && !isNetworkEnabled) {
//                throw LocationClient.LocationException("GPS is disabled")
//            }
//
//            val request = LocationRequest.create()
//                .setInterval(interval)
//                .setFastestInterval(interval / 4)
//
//            val locationCallback = object : LocationCallback() {
//                override fun onLocationResult(result: LocationResult) {
//                    super.onLocationResult(result)
//                    result.locations.lastOrNull()?.let { location ->
//                        println(">>> Info: Sending location update...")
//                        // var liveLocation = LiveLocation(location.latitude.toString(), location.longitude.toString())
//                        launch { send(location) }
//                    }
//                }
//            }
//
//            client.requestLocationUpdates(
//                request,
//                locationCallback,
//                Looper.getMainLooper()
//            )
//
//            awaitClose {
//                client.removeLocationUpdates(locationCallback)
//            }
//        }
//    }
//}