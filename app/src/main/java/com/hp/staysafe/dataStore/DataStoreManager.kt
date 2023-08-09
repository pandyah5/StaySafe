package com.hp.staysafe.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("locationStatus")

class DataStoreManager(context: Context, dataStoreName: String) {
    private val dataStore = context.dataStore

    suspend fun update(key: String, value: String) {
        // If the key is absent we create a new one, else we modify the current value
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun read(key:String) : Flow<String> {
        val dataStoreKey = stringPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val uiMode = preferences[dataStoreKey] ?: "NONE"
                uiMode
            }
    }
}