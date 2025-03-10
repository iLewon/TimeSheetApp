package com.mobileexam.timesheetapp.models

import kotlinx.serialization.Serializable

@Serializable
data class TimesheetEntry(
    val date: String,
    val timeStart: String,
    val timeEnd: String
)
