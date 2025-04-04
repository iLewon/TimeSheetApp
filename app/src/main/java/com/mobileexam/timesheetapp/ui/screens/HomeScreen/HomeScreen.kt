package com.mobileexam.timesheetapp.ui.screens.HomeScreen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.mobileexam.timesheetapp.models.LogItem
import com.mobileexam.timesheetapp.ui.screens.TimesheetHistory.formatDate
import com.mobileexam.timesheetapp.utils.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun HomeScreen(modifier: Modifier, navController: NavController, context: Context, viewModel: HomeScreenViewModel = viewModel()) {
    val isClockedIn by viewModel.isClockedIn.collectAsState()
    val isOnBreak by viewModel.isOnBreak.collectAsState()
    val dutySeconds by viewModel.dutySeconds.collectAsState()
    val logsResponse by viewModel.logs.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()
    var selectedEntry by remember { mutableStateOf<LogItem?>(null) }

    var currentTime by remember { mutableStateOf(getCurrentTime()) }

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
            delay(1000)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchLogs()
    }


    val currentDate = getCurrentDate()

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
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.onTertiary, shape = RoundedCornerShape(12.dp)),
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
                            Icon(painter = painterResource(R.drawable.calendar), contentDescription = "Calendar", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Today is $currentDate", color = MaterialTheme.colorScheme.onSurface)
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
                        Text("You are currently clocked out.", color = MaterialTheme.colorScheme.onSurface)
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    if (!isClockedIn) {
                        Button(
                            onClick = { viewModel.clockIn() },
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
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(if (isOnBreak) "END BREAK" else "START BREAK", color = Color.Black)
                            }
                            Button(
                                onClick = { viewModel.clockOut() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("CLOCK OUT", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
        item {
            //ADD THE TIMESHEET HISTORY DATA ENTRIES HERE
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.onTertiary, shape = RoundedCornerShape(12.dp)),
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.DarkGray)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Date", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text("Time Start", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text("Time End", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    }

                    logsResponse?.response?.Logs?.let { logs ->
                        val recentLogs = logs.takeLast(5)

                        LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)) {
                            items(recentLogs.size) { index ->
                                val entry = recentLogs[index]
                                val formattedDate = formatDate(entry.date)
                                val formattedTimeStart = formatTime(entry.timeIn)
                                val formattedTimeEnd = formatTime(entry.timeOut)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedEntry = entry }
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(formattedDate, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                                    Text(formattedTimeStart, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                                    Text(formattedTimeEnd, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    } ?: Text("No logs available", modifier = Modifier.padding(16.dp))

                    if (errorMessage != null) {
                        Text("Error: $errorMessage", color = Color.Red)
                    }
                }
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
}

fun formatTime(timestamp: Long): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(timestamp))
}

@Composable
fun TimesheetDetailDialog(entry: LogItem, onClose: () -> Unit) {
    val totalHoursWorked = calculateHoursWorked(formatTime(entry.timeIn), formatTime(entry.timeOut))

    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            Button(onClick = onClose) {
                Text("Close")
            }
        },
        title = { Text("Timesheet Details", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Date: ${formatDate(entry.date)}")
                Text("Time Started: ${formatTime(entry.timeIn)}")
                Text("Time Ended: ${formatTime(entry.timeOut)}")
                Text("Total Hours Worked: $totalHoursWorked")
            }
        }
    )
}

fun calculateHoursWorked(startTime: String, endTime: String): String {
    return try {
        val format = SimpleDateFormat("hh:mm a", Locale.US)
        val start = format.parse(startTime)
        val end = format.parse(endTime)
        val diff = end.time - start.time
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
        "${hours}h ${minutes}m"
    } catch (e: Exception) {
        "N/A"
    }
}