package com.mobileexam.timesheetapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobileexam.timesheetapp.data.api.RetrofitClient
import com.mobileexam.timesheetapp.models.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    fun login(email: String, password: String, context: Context, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val call = RetrofitClient.instance.login(email, password)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.status == "success") {
                            val token = loginResponse.response.token
                            val userId = loginResponse.response.user_id

                            // Save token in SharedPreferences
                            val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit().putString("auth_token", token).apply()

                            println("Login Successful! Token: $token, User ID: $userId")
                            onResult(true)
                        } else {
                            println("Login Failed: ${response.message()}")
                            onResult(false)
                        }
                    } else {
                        println("API Error: ${response.errorBody()?.string()}")
                        onResult(false)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    println("API Request Failed: ${t.message}")
                    onResult(false)
                }
            })
        }
    }
}
