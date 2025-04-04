package com.mobileexam.timesheetapp.ui.screens.TimesheetHistory

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
import com.mobileexam.timesheetapp.viewmodel.TimesheetHistoryViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun TimesheetHistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    context: Context,
    viewModel: TimesheetHistoryViewModel = viewModel()
) {
    val logsResponse by viewModel.logs.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()
    var selectedEntry by remember { mutableStateOf<LogItem?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchLogs()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column {
            Text(
                "Timesheet History",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(logs.size) { index ->
                                val entry = logs[index]
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

    selectedEntry?.let { entry ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            TimesheetDetailDialog(entry) { selectedEntry = null }
        }
    }
}

fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
}

fun formatTime(timestamp: Long): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(timestamp))
}

fun calculateHoursAndMinutesWorked(timeIn: Long, timeOut: Long): String {
    val durationMillis = timeOut - timeIn
    val totalMinutes = durationMillis / (1000 * 60)
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return "$hours hr ${minutes} min"
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
                Text("Attendance Status: ${entry.attendanceStatus}")
                Text("Time Started: ${formatTime(entry.timeIn)}")
                Text("Time Ended: ${formatTime(entry.timeOut)}")
                Text("Total Hours Worked: ${calculateHoursAndMinutesWorked(entry.timeIn, entry.timeOut)}")

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