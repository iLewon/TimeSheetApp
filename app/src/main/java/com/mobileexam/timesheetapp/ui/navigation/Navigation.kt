package com.mobileexam.timesheetapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobileexam.timesheetapp.ui.components.BottomNavigationBar
import com.mobileexam.timesheetapp.ui.screens.HomeScreen.HomeScreen
import com.mobileexam.timesheetapp.ui.screens.HomeScreen.HomeScreenViewModel
import com.mobileexam.timesheetapp.ui.screens.LoginScreen.LoginScreen
import com.mobileexam.timesheetapp.ui.screens.ProfileScreen.ProfileScreen
import com.mobileexam.timesheetapp.ui.screens.TimesheetHistory.TimesheetHistoryScreen
import com.mobileexam.timesheetapp.ui.viewmodel.ProfileViewModel
import com.mobileexam.timesheetapp.viewmodel.TimesheetHistoryViewModel


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
            composable("history") {
                val timesheetHistoryViewModel: TimesheetHistoryViewModel = viewModel()
                TimesheetHistoryScreen(
                    modifier = Modifier,
                    navController = navController,
                    context = context,
                    viewModel = timesheetHistoryViewModel
                )
            }
            composable("profile") {
                val profileViewModel: ProfileViewModel = viewModel()
                ProfileScreen(navController, profileViewModel)
            }

        }
    }
}


