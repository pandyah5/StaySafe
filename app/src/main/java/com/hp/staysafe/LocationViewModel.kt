package com.hp.staysafe

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.hp.staysafe.data.LocationLiveData
import kotlinx.coroutines.flow.MutableStateFlow

class LocationViewModel(application : Application) : AndroidViewModel(application) {
    private val locationLiveData = LocationLiveData(application)

    fun getLocationLiveData () : LocationLiveData {
        return locationLiveData
    }

    fun startLocationUpdates () {
        locationLiveData.startLocationUpdates()
    }
}