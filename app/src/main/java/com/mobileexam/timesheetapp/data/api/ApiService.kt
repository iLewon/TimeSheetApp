package com.mobileexam.timesheetapp.data.api

import com.mobileexam.timesheetapp.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")  // Replace with your actual login endpoint
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("users")  // Replace with your actual users endpoint
    fun getUsers(@Header("Authorization") token: String): Call<LoginResponse>
}
