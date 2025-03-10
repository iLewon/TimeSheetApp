package com.mobileexam.timesheetapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobileexam.timesheetapp.navigation.TimesheetApp
import com.mobileexam.timesheetapp.ui.components.BottomNavigationBar
import com.mobileexam.timesheetapp.ui.screens.HomeScreen.HomeScreen
import com.mobileexam.timesheetapp.ui.screens.ProfileScreen.ReportsScreen
import com.mobileexam.timesheetapp.ui.screens.TimesheetHistory.TimesheetHistoryScreen
import com.mobileexam.timesheetapp.ui.theme.TimeSheetAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeSheetAppTheme(dynamicColor = false) {
                TimesheetApp()
            }
        }
    }
}

