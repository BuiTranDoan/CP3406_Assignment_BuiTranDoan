package com.studyhelpers.app.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAddTaskClick: () -> Unit,
    onTaskClick: (Int) -> Unit,
    onFocusTimerClick: () -> Unit,
    currentUserName: String? = null
) {
    val uiState = viewModel.uiState.collectAsState().value
    val showDialog = uiState.showDeleteDialog

    val tasksToShow = if (uiState.hideCompleted) {
        uiState.tasks.filter { !it.isCompleted }
    } else {
        uiState.tasks
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Study Helpers",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = onFocusTimerClick) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = "Focus Timer"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTaskClick,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Greeting line (if user is logged in)
            if (currentUserName != null) {
                Text(
                    text = "Hi, $currentUserName",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = "Your Tasks",
                style = MaterialTheme.typography.headlineSmall
            )

            // ---- Quote section (optional) ----
            val quote = uiState.quote
            val author = uiState.quoteAuthor

            if (quote != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"$quote\"",
                    style = MaterialTheme.typography.bodyMedium
                )
                author?.let {
                    Text(
                        text = "- $it",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Hide completed", style = MaterialTheme.typography.bodyMedium)

                Switch(
                    checked = uiState.hideCompleted,
                    onCheckedChange = { checked ->
                        viewModel.setHideCompleted(checked)
                    }
                )
            }

            // Delete confirmation dialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissDeleteDialog() },
                    confirmButton = {
                        TextButton(onClick = { viewModel.confirmDelete() }) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Delete Task?") },
                    text = { Text("This action cannot be undone.") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(tasksToShow) { task ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable { onTaskClick(task.id) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    task.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                task.description?.let {
                                    Text(
                                        it,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                // ---- Due date ----
                                task.dueDate?.let { millis ->
                                    val formatter = SimpleDateFormat(
                                        "dd MMM yyyy",
                                        Locale.getDefault()
                                    )
                                    Text(
                                        text = "Due: ${formatter.format(Date(millis))}",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }

                                // ---- Priority ----
                                val priorityLabel = when (task.priority) {
                                    "MAJOR" -> "Priority: Major"
                                    "NORMAL" -> "Priority: Normal"
                                    "MINOR" -> "Priority: Minor"
                                    else -> "Priority: Normal"
                                }
                                Text(
                                    text = priorityLabel,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            Row {
                                Checkbox(
                                    checked = task.isCompleted,
                                    onCheckedChange = { checked ->
                                        viewModel.onTaskCheckedChanged(task, checked)
                                    }
                                )

                                IconButton(onClick = { viewModel.requestDelete(task) }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete task"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}