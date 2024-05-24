package com.example.didyoufeedthedog

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.runBlocking
import java.text.DateFormat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.*
class FeedingsViewModel(application: Application) : AndroidViewModel(application) {
    var state by mutableStateOf(MyAppState())
    init {
        // Load saved feedings list.
        val feedings = runBlocking {
            application.applicationContext.dataStore.data.map { preferences ->
                preferences[stringSetPreferencesKey("feedings")]
            }.first()
        }

        if (feedings != null) {
            state = MyAppState(feedings.toMutableList())
        }
    }

    // Returns the list of text values associated with the last five feedings.
    fun addFeeding(
        context: Context,
        feedings: MutableList<String>
    ) {
        val time = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date())
        val date = DateFormat.getDateInstance().format(Date())

        val feedingText = "Dog fed at $time on $date"

        if (feedings.contains(feedingText)) {
            val text = "You already fed the dog"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, text, duration)
            toast.show()
        } else {
            feedings.add(0, feedingText)
            if (feedings.size > 5) { feedings.removeAt(feedings.size - 1) }
        }
        state = MyAppState(feedings)
    }

    fun removeFeeding(
        feedings: MutableList<String>,
        feeding: String
    ) {
        feedings.remove(feeding)
        state = MyAppState(feedings)
    }
}