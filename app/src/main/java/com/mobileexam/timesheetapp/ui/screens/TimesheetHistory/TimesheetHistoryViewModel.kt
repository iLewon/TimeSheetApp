package com.mobileexam.timesheetapp.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobileexam.timesheetapp.data.api.RetrofitClient
import com.mobileexam.timesheetapp.models.LogsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TimesheetHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val _logs = MutableLiveData<LogsResponse?>()
    val logs: LiveData<LogsResponse?> get() = _logs

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private fun getAuthToken(): String {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    fun fetchLogs() {
        val token = getAuthToken()
        if (token.isEmpty()) {
            _errorMessage.postValue("No authentication token found! Please log in.")
            return
        }

        RetrofitClient.instance.getLogs("Bearer $token").enqueue(object : Callback<LogsResponse> {
            override fun onResponse(call: Call<LogsResponse>, response: Response<LogsResponse>) {
                if (response.isSuccessful) {
                    _logs.postValue(response.body())
                    Log.d("TimeSheetHistoryVM", "Logs fetched successfully")
                } else {
                    _errorMessage.postValue("Error: ${response.errorBody()?.string()}")
                    Log.e("TimeSheetHistoryVM", "Error fetching logs: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<LogsResponse>, t: Throwable) {
                _errorMessage.postValue("Failed to fetch logs: ${t.message}")
                Log.e("TimeSheetHistoryVM", "API Call Failed: ${t.message}")
            }
        })
    }
}