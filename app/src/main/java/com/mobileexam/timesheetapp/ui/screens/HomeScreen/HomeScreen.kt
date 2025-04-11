package com.mobileexam.timesheetapp.ui.screens.HomeScreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobileexam.timesheetapp.R
import com.mobileexam.timesheetapp.models.LogItem
import com.mobileexam.timesheetapp.ui.screens.TimesheetHistory.loadLocalTimesheetLogs
import com.mobileexam.timesheetapp.utils.*
import kotlinx.coroutines.delay
import java.io.InputStreamReader
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

    val logs = remember { com.mobileexam.timesheetapp.ui.screens.HomeScreen.loadLocalTimesheetLogs(context) }

    val location by viewModel.location

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.fetchLocation()
        } else {
            Toast.makeText(context, "Location permission is needed to clock in with location.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(isClockedIn) {
        if (isClockedIn) {
            viewModel.fetchLocation()
        }
    }

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

    val currentDate = getCurrentDate()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Greeting Header
        item {
            Text(
                "Hello, User!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Clock-in/Clock-out Section
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
                        if (location != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.location),
                                        contentDescription = "Location",
                                        modifier = Modifier.size(20.dp),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        " $location",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else {
                        Text("You are currently clocked out.", color = MaterialTheme.colorScheme.onSurface)
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    if (!isClockedIn) {
                        Button(
                            onClick = {
                                val fineLocationGranted = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED

                                if (!fineLocationGranted) {
                                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                } else {
                                    viewModel.fetchLocation()
                                }

                                viewModel.clockIn()
                            },
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

        // Timesheet History Section
        // Timesheet History Section
        item {
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

                    // Use the logs variable directly
                    if (logs.isEmpty()) {
                        Text("No timesheet logs available.", color = MaterialTheme.colorScheme.onSurface)
                    } else {
                        val recentLogs = logs.takeLast(5)
                        LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)) {
                            items(recentLogs) { entry ->
                                val formattedDate = if (entry.date.isNullOrEmpty()) "--" else entry.date
                                val formattedTimeStart = if (entry.timeIn.isNullOrEmpty()) "--" else entry.timeIn
                                val formattedTimeEnd = if (entry.timeOut.isNullOrEmpty()) "--" else entry.timeOut

                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedEntry = entry }
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        listOf(formattedDate, formattedTimeStart, formattedTimeEnd).forEach { text ->
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxWidth(),
                                                contentAlignment = if (text == "--") Alignment.Center else Alignment.CenterStart
                                            ) {
                                                Text(
                                                    text,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    fontSize = 13.sp
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun loadLocalTimesheetLogs(context: Context): List<LogItem> {
    return try {
        val assetManager = context.assets
        val inputStream = assetManager.open("timesheet.json")
        val reader = InputStreamReader(inputStream)
        val logItemType = object : TypeToken<List<LogItem>>() {}.type
        Gson().fromJson(reader, logItemType)
    } catch (e: Exception) {
        e.printStackTrace() // Log the exception
        emptyList()
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
                Text("Date: ${(entry.date)}")
                Text("Attendance Status: ${entry.status}")
                Text("Time Started: ${(entry.timeIn)}")
                Text("Time Ended: ${(entry.timeOut)}")
                Text("Total Under-time: ${entry.totalUndertime}")
            }
        }
    )
}


//fun calculateHoursWorked(startTime: String, endTime: String): String {
//    return try {
//        val format = SimpleDateFormat("hh:mm a", Locale.US)
//        val start = format.parse(startTime)
//        val end = format.parse(endTime)
//        val diff = end.time - start.time
//        val hours = TimeUnit.MILLISECONDS.toHours(diff)
//        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
//        "${hours}h ${minutes}m"

