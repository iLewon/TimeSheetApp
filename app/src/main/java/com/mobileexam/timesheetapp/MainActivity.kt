package com.mobileexam.timesheetapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.mobileexam.timesheetapp.navigation.TimesheetApp
import com.mobileexam.timesheetapp.ui.theme.TimeSheetAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeSheetAppTheme(dynamicColor = false) {
                val navController = rememberNavController()
                TimesheetApp()
            }
        }
    }
}

