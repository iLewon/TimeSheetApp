package com.mobileexam.timesheetapp.ui.screens.HomeScreen

import HomeScreenViewModel
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mobileexam.timesheetapp.R
import com.mobileexam.timesheetapp.models.TimesheetEntry
import com.mobileexam.timesheetapp.utils.formatTime
import com.mobileexam.timesheetapp.utils.getCurrentDate
import com.mobileexam.timesheetapp.utils.getCurrentTime
import com.mobileexam.timesheetapp.utils.loadTimesheetData
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(modifier: Modifier, navController: NavController, context: Context, viewModel: HomeScreenViewModel = viewModel()) {
    val isClockedIn by viewModel.isClockedIn.collectAsState()
    val isOnBreak by viewModel.isOnBreak.collectAsState()
    val dutySeconds by viewModel.dutySeconds.collectAsState()

    var currentTime by remember { mutableStateOf(getCurrentTime()) }
    var timesheetEntries by remember { mutableStateOf<List<TimesheetEntry>>(emptyList()) }

    LaunchedEffect(isClockedIn) {
        if (isClockedIn) {
            viewModel.startDutyTimer()
        } else {
            viewModel.stopDutyTimer()
        }
    }


    LaunchedEffect(Unit) {
        while (true) {
            currentTime = getCurrentTime()
            kotlinx.coroutines.delay(1000)
        }
    }

    LaunchedEffect(Unit) {
        val allEntries = loadTimesheetData(context)
        timesheetEntries = allEntries.sortedByDescending { it.date }.take(5)
    }

    val currentDate = getCurrentDate()
//    val dutyTime = getCurrentTime()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Hello, User!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }
//        Spacer(modifier = Modifier.height(16.dp))
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onTertiary,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.calendar),
                                contentDescription = "Calendar",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Today is $currentDate",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(currentTime, color = MaterialTheme.colorScheme.onSurface)
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    if (isClockedIn) {
                        Text(
                            formatTime(dutySeconds),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            "You are currently clocked out.",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    if (!isClockedIn) {
                        Button(
                            onClick = {viewModel.clockIn()},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("CLOCK IN", color = Color.White)
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { viewModel.toggleBreak() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFFFC107
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    if (isOnBreak) "END BREAK" else "START BREAK",
                                    color = Color.Black
                                )
                            }

                            Button(
                                onClick = {viewModel.clockOut()},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFD32F2F
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("CLOCK OUT", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
//        Spacer(modifier = Modifier.height(16.dp))
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onTertiary,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.time),
                            contentDescription = "History",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Timesheet History", color = MaterialTheme.colorScheme.onSurface)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.DarkGray)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Date",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "Time Start",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "Time End",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Table Rows
                    timesheetEntries.forEach { entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(entry.date, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                            Text(entry.timeStart, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                            Text(entry.timeEnd, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.navigate("history") },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Show Timesheet", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}