package com.example.didyoufeedthedog

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "feedingsData")

data class MyAppState(val feedings: MutableList<String> = mutableListOf())