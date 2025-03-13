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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobileexam.timesheetapp.R
import com.mobileexam.timesheetapp.models.TimesheetEntry
import com.mobileexam.timesheetapp.utils.loadTimesheetData
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun TimesheetHistoryScreen(modifier: Modifier, navController: NavController, context: Context) {
    var timesheetEntries by remember { mutableStateOf<List<TimesheetEntry>>(emptyList()) }
    var selectedEntry by remember { mutableStateOf<TimesheetEntry?>(null) }

    // Load all timesheet data
    LaunchedEffect(Unit) {
        timesheetEntries = loadTimesheetData(context).sortedByDescending { it.date }
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

                    // Table Header
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

                    // Table Rows
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(timesheetEntries.size) { index ->
                            val entry = timesheetEntries[index]
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedEntry = entry } // Make row pressable
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(entry.date, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                                Text(entry.timeStart, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                                Text(entry.timeEnd, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }

    // Show Details Dialog when a row is clicked
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

@Composable
fun TimesheetDetailDialog(entry: TimesheetEntry, onClose: () -> Unit) {
    val totalHoursWorked = calculateHoursWorked(entry.timeStart, entry.timeEnd)

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
                Text("Date: ${entry.date}")
                Text("Time Started: ${entry.timeStart}")
                Text("Time Ended: ${entry.timeEnd}")
                Text("Total Hours Worked: $totalHoursWorked hours")
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
