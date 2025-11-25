package com.studyhelpers.app.ui.calendar

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studyhelpers.app.data.local.entity.TaskEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onTaskClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendar") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // Month header + arrows
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.onPrevMonth() }) {
                    Icon(Icons.Filled.ChevronLeft, contentDescription = "Previous month")
                }
                Text(
                    text = uiState.monthTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { viewModel.onNextMonth() }) {
                    Icon(Icons.Filled.ChevronRight, contentDescription = "Next month")
                }
            }

            Spacer(Modifier.height(8.dp))

            // Weekday headers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Calendar grid
            val cells: List<CalendarDay?> = buildList {
                repeat(uiState.leadingEmptyCells) { add(null) }
                uiState.days.forEach { add(it) }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                items(cells) { cell ->
                    if (cell == null) {
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                        )
                    } else {
                        val isSelected = uiState.selectedDate?.let { sel ->
                            cell.timeMillis == sel
                        } == true

                        DayCell(
                            day = cell,
                            isSelected = isSelected,
                            onClick = { viewModel.onDaySelected(cell.timeMillis) }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Tasks on this day",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            if (uiState.tasksForSelected.isEmpty()) {
                Text(
                    text = "No tasks due on this day.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn {
                    items(uiState.tasksForSelected.size) { index ->
                        val task = uiState.tasksForSelected[index]
                        TaskRow(
                            task = task,
                            onClick = { onTaskClick(task.id) },
                            onAddToCalendar = {
                                task.dueDate?.let { millis ->
                                    insertIntoDeviceCalendar(context, task, millis)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when {
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
        day.hasTasks -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
        else -> MaterialTheme.colorScheme.surface
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .background(bgColor, shape = MaterialTheme.shapes.small)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (day.hasTasks) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun TaskRow(
    task: TaskEntity,
    onClick: () -> Unit,
    onAddToCalendar: () -> Unit
) {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.titleMedium)
                task.description?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
                task.dueDate?.let { millis ->
                    Text(
                        text = "Due at ${formatter.format(Date(millis))}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            IconButton(onClick = onAddToCalendar) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = "Add to Google Calendar"
                )
            }
        }
    }
}
private fun insertIntoDeviceCalendar(
    context: android.content.Context,
    task: TaskEntity,
    startMillis: Long
) {
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, task.title)
        putExtra(CalendarContract.Events.DESCRIPTION, task.description ?: "")
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startMillis + 60 * 60 * 1000)
    }
    context.startActivity(intent)
}