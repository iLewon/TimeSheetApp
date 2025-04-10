package com.mobileexam.timesheetapp.data.api

import com.mobileexam.timesheetapp.models.LoginResponse
import com.mobileexam.timesheetapp.models.LogsResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("users")
    fun getUsers(@Header("Authorization") token: String): Call<LoginResponse>

//    @GET("logs")
//    fun getLogs(@Header("Authorization") token: String): Call<LogsResponse>

    @GET("logs")
    fun getLogs(@Header("Authorization") token: String): Call<List<LogsResponse>>

}