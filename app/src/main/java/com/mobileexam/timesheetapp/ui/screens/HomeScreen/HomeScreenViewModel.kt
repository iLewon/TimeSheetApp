package com.mobileexam.timesheetapp.ui.screens.HomeScreen

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.mobileexam.timesheetapp.data.api.RetrofitClient
import com.mobileexam.timesheetapp.models.LogsResponse
import com.mobileexam.timesheetapp.models.TimesheetEntry
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val _isClockedIn = MutableStateFlow(false)
    val isClockedIn = _isClockedIn.asStateFlow()

    private val _isOnBreak = MutableStateFlow(false)
    val isOnBreak = _isOnBreak.asStateFlow()

    private val _dutySeconds = MutableStateFlow(0)
    val dutySeconds = _dutySeconds.asStateFlow()

    private var dutyTimerJob: Job? = null

//    private val _logs = MutableLiveData<LogsResponse?>()
//    val logs: LiveData<LogsResponse?> get() = _logs

    private val _logs = MutableLiveData<List<LogsResponse>?>()
    val logs: LiveData<List<LogsResponse>?> get() = _logs

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private fun getAuthToken(): String {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private val context = application.applicationContext
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val _location = mutableStateOf<String?>(null)
    val location: State<String?> = _location

    fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            _location.value = "Location permission not granted"
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(context, Locale.getDefault())
                val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                _location.value = address?.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
            } else {
                _location.value = "Unable to get location"
            }
        }.addOnFailureListener {
            _location.value = "Location error: ${it.localizedMessage}"
        }
    }

    fun fetchLogs() {
//        val token = getAuthToken()
//        if (token.isEmpty()) {
//            _errorMessage.postValue("No authentication token found! Please log in.")
//            return
//        }
//
//        RetrofitClient.instance.getLogs("Bearer $token").enqueue(object : Callback<LogsResponse> {
//            override fun onResponse(call: Call<LogsResponse>, response: Response<LogsResponse>) {
//                if (response.isSuccessful) {
//                    _logs.postValue(response.body())
//                    Log.d("HomeTimeSheetHistoryVM", "Logs fetched successfully")
//                } else {
//                    _errorMessage.postValue("Error: ${response.errorBody()?.string()}")
//                    Log.e("HomeTimeSheetHistoryVM", "Error fetching logs: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<LogsResponse>, t: Throwable) {
//                _errorMessage.postValue("Failed to fetch logs: ${t.message}")
//                Log.e("TimeSheetHistoryVM", "API Call Failed: ${t.message}")
//            }
//        })

        val token = getAuthToken()
        if (token.isEmpty()) {
            _errorMessage.postValue("No authentication token found! Please log in.")
            return
        }

        RetrofitClient.instance.getLogs("Bearer $token").enqueue(object : Callback<List<LogsResponse>> {
            override fun onResponse(call: Call<List<LogsResponse>>, response: Response<List<LogsResponse>>) {
                if (response.isSuccessful) {
                    _logs.postValue(response.body())
                } else {
                    _errorMessage.postValue("Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<LogsResponse>>, t: Throwable) {
                _errorMessage.postValue("Failed to fetch logs: ${t.message}")
            }
        })
    }


    fun clockIn() {
        _isClockedIn.value = true
        _dutySeconds.value = 0
        startDutyTimer()
    }

    fun clockOut() {
        _isClockedIn.value = false
        _isOnBreak.value = false
        stopDutyTimer()
        _dutySeconds.value = 0
    }

    fun toggleBreak() {
        _isOnBreak.value = !_isOnBreak.value
    }

    fun startDutyTimer() {  // Changed from private to public
        stopDutyTimer() // Cancel any existing timer before starting a new one

        dutyTimerJob = viewModelScope.launch {
            while (_isClockedIn.value) {
                delay(1000)
                _dutySeconds.update { it + 1 }
            }
        }
    }

    fun stopDutyTimer() {  // Changed from private to public
        dutyTimerJob?.cancel()
        dutyTimerJob = null
    }
}
