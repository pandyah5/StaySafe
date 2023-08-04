package com.hp.staysafe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hp.staysafe.data.LocationLiveData

class LocationViewModel(application : Application) : AndroidViewModel(application) {
    private val locationLiveData = LocationLiveData(application)
    fun getLocationLiveData () : LocationLiveData {
        return locationLiveData
    }

    fun startLocationUpdates () {
        locationLiveData.startLocationUpdates()
    }
}