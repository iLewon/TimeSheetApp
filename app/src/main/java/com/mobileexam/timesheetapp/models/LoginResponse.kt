package com.mobileexam.timesheetapp.models

data class LoginResponse(
    val status: String,
    val response: TokenData
)

data class TokenData(
    val token: String,
    val user_id: String,
    val expires: Int
)

