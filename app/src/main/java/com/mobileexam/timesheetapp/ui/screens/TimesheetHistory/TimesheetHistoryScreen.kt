package com.mobileexam.timesheetapp.ui.screens.TimesheetHistory

import android.content.Context
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobileexam.timesheetapp.R
import com.mobileexam.timesheetapp.models.LogItem
import com.mobileexam.timesheetapp.viewmodel.TimesheetHistoryViewModel
import java.io.InputStreamReader
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
    var selectedEntry by remember { mutableStateOf<LogItem?>(null) }
    val logs = remember { loadLocalTimesheetLogs(context) }

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

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(logs) { entry ->
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

// Loads data from assets/timesheet.json
fun loadLocalTimesheetLogs(context: Context): List<LogItem> {
    return try {
        val assetManager = context.assets
        val inputStream = assetManager.open("timesheet.json")
        val reader = InputStreamReader(inputStream)
        val logItemType = object : TypeToken<List<LogItem>>() {}.type
        Gson().fromJson(reader, logItemType)
    } catch (e: Exception) {
        emptyList()
    }
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
                Text("Date: ${entry.date}")
                Text("Attendance Status: ${entry.status}")
                Text("Time Started: ${entry.timeIn}")
                Text("Time Ended: ${entry.timeOut}")
                Text("Total Under-time: ${entry.totalUndertime}")
            }
        }
    )
}