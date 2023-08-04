package com.hp.staysafe

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class LocationViewModel(application : Application) : AndroidViewModel(application) {
    private val locationLiveData = LocationLiveData(application)
    fun getLocationLiveData () : LocationLiveData {
        return locationLiveData
    }

    fun getLocationNeighbourhoodData () : String {
        return locationLiveData.getNeighbourhood()
    }

    fun startLocationUpdates () {
        locationLiveData.startLocationUpdates()
    }
}