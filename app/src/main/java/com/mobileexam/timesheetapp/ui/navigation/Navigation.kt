package com.mobileexam.timesheetapp.navigation

import HomeScreenViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.mobileexam.timesheetapp.R
import com.mobileexam.timesheetapp.ui.components.BottomNavigationBar
import com.mobileexam.timesheetapp.ui.screens.HomeScreen.HomeScreen
import com.mobileexam.timesheetapp.ui.screens.LoginScreen.LoginScreen
import com.mobileexam.timesheetapp.ui.screens.ProfileScreen.ReportsScreen
import com.mobileexam.timesheetapp.ui.screens.TimesheetHistory.TimesheetHistoryScreen



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimesheetApp() {
    val navController = rememberNavController()

    val currentRoute by navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)

    val showBottomBar = currentRoute?.destination?.route !in listOf("login", null)
    val showTopBar = showBottomBar // Hide top bar on login too

    //Initialize ViewModel once and share it
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = {
                        Text("Jairosoft", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(padding)
        ) {
            composable("login") { LoginScreen(navController) }
            composable("home") { HomeScreen(
                modifier = Modifier,
                navController = navController,
                context = context,
                viewModel = homeScreenViewModel) }
            composable("history") { TimesheetHistoryScreen(
                modifier = Modifier,
                navController = navController,
                context = context) }
            composable("profile") { ReportsScreen(navController) }
        }
    }
}


