package com.mobileexam.timesheetapp.utils

import android.content.Context
import android.util.Log
import com.mobileexam.timesheetapp.models.TimesheetEntry
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun loadTimesheetData(context: Context): List<TimesheetEntry> {
    return try {
        val jsonString = context.assets.open("timesheet.json").bufferedReader().use { it.readText() }
        Log.d("TimesheetData", "JSON Loaded: $jsonString") // Log the raw JSON data
        Json.decodeFromString(jsonString)
    } catch (e: Exception) {
        Log.e("TimesheetData", "Error loading JSON", e) // Log the error if it fails
        emptyList()
    }
}

