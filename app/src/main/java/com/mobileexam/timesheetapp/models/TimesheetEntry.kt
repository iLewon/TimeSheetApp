package com.mobileexam.timesheetapp.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TimesheetEntry(
    val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("start_time") val timeStart: String,
    @SerializedName("end_time") val timeEnd: String,
    val attendanceStatus: String // Add other necessary fields
)