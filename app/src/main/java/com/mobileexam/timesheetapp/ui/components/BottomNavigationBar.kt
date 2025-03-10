package com.mobileexam.timesheetapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mobileexam.timesheetapp.R

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = currentBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.home), contentDescription = "Home", modifier = Modifier.size(24.dp)) },
            label = { Text("Home") },
            selected = currentScreen == "home",
            onClick = { navController.navigate("home") },

        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.timesheet), contentDescription = "History", modifier = Modifier.size(24.dp)) },
            label = { Text("History") },
            selected = currentScreen == "history",
            onClick = { navController.navigate("history") },
            modifier = Modifier.size(30.dp)
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.profile_picture), contentDescription = "Profile", modifier = Modifier.size(24.dp)) },
            label = { Text("Profile") },
            selected = currentScreen == "profile",
            onClick = { navController.navigate("profile") },
            modifier = Modifier.size(30.dp)
        )
    }
}



