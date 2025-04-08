package com.mobileexam.timesheetapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.mobileexam.timesheetapp.navigation.TimesheetApp
import com.mobileexam.timesheetapp.ui.theme.TimeSheetAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check token from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        val isLoggedIn = token != null

        setContent {
            TimeSheetAppTheme(dynamicColor = false) {
                TimesheetApp(isLoggedIn) // pass the flag to the nav system
            }
        }
    }
}


