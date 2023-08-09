package com.hp.staysafe.dataStore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocStatusViewModel (application : Application) : AndroidViewModel(application) {
    private val dataStore = DataStoreManager(application, "locationStatus")
    private val locaStatusKey = "LOC_STATUS"

    val getStatus = dataStore.read(locaStatusKey).asLiveData(Dispatchers.IO)

    fun setStatus(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.update(locaStatusKey, value)
        }
    }
}