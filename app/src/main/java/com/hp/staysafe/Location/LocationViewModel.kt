package com.hp.staysafe.Location

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hp.staysafe.Location.LocationLiveData

class LocationViewModel(application : Application) : AndroidViewModel(application) {
    private val locationLiveData = LocationLiveData(application)

    fun getLocationLiveData () : LocationLiveData {
        return locationLiveData
    }

    fun startLocationUpdates () {
        locationLiveData.startLocationUpdates()
    }
}