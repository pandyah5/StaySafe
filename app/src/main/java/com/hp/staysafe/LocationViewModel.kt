package com.hp.staysafe

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class LocationViewModel : ViewModel() {

    var userLocationLat by mutableStateOf(0.0)
        private set

    var userLocationLon by mutableStateOf(0.0)
        private set

    fun updateUserLocation() {
        userLocationLat = 10.0
        userLocationLon = -10.0

    }
}