package com.mobileexam.timesheetapp.navigation

import HomeScreenViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobileexam.timesheetapp.R
import com.mobileexam.timesheetapp.ui.components.BottomNavigationBar
import com.mobileexam.timesheetapp.ui.screens.HomeScreen.HomeScreen
import com.mobileexam.timesheetapp.ui.screens.ProfileScreen.ProfileScreen
import com.mobileexam.timesheetapp.ui.screens.TimesheetHistory.TimesheetHistoryScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimesheetApp() {
    val navController = rememberNavController()
    val montserratFont = FontFamily(Font(R.font.montserrat_medium))
    val context = LocalContext.current

    // Initialize ViewModel once and share it
    val homeScreenViewModel: HomeScreenViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.jairo_logo),
                                contentDescription = "Jairosoft Logo",
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Jairosoft",
                                fontFamily = montserratFont,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            // ✅ Pass the shared ViewModel instead of creating a new one
            composable("home") {
                HomeScreen(
                    modifier = Modifier,
                    navController = navController,
                    context = context,
                    viewModel = homeScreenViewModel // Using shared ViewModel
                )
            }
            composable("history") { TimesheetHistoryScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
        }
    }
}
