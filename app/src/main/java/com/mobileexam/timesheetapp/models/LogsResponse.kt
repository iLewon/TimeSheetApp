package com.mobileexam.timesheetapp.models

import com.google.gson.annotations.SerializedName

data class LogsResponse(
    val status: String,  // "success" or other status
    val response: LogsData
)

data class LogsData(
    val Logs: List<LogItem>
)


data class LogItem(
    @SerializedName("_id") val id: String,
    @SerializedName("Total Hours 100%") val totalHours: Long,
    @SerializedName("toggle") val toggle: String,
    @SerializedName("time-in") val timeIn: Long,
    @SerializedName("time-out") val timeOut: Long,
    @SerializedName("Attendance Status") val attendanceStatus: String,
    @SerializedName("Created Date") val createdDate: Long,
    @SerializedName("Date") val date: Long,
    @SerializedName("Created By") val createdBy: String,
    @SerializedName("Modified Date") val modifiedDate: Long,
    @SerializedName("User_id") val userId: String
)

