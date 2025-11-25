package com.studyhelpers.app.ui.addtask

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    onBack: () -> Unit,
    viewModel: AddTaskViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // 👇 this will store the selected due date as a timestamp
    var dueDateMillis by remember { mutableStateOf<Long?>(null) }
    var dueDateDisplay by remember { mutableStateOf("Pick a due date") }

    var priorityKey by remember { mutableStateOf("NORMAL") }
    var priorityExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    val priorityOptions = listOf(
        "MAJOR" to "Major (exams, finals)",
        "NORMAL" to "Normal (tests, assignments)",
        "MINOR" to "Minor (daily homework)"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Task") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---- Due date picker ----
            Text(
                text = dueDateDisplay,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH)
                        val day = calendar.get(Calendar.DAY_OF_MONTH)

                        DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                calendar.set(y, m, d, 0, 0, 0)
                                calendar.set(Calendar.MILLISECOND, 0)
                                dueDateMillis = calendar.timeInMillis
                                dueDateDisplay = "$d/${m + 1}/$y"
                            },
                            year,
                            month,
                            day
                        ).show()
                    }
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---- Priority dropdown ----
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Priority: ${
                        priorityOptions.first { it.first == priorityKey }.second
                    }",
                    modifier = Modifier
                        .clickable { priorityExpanded = true }
                        .padding(vertical = 8.dp)
                )

                DropdownMenu(
                    expanded = priorityExpanded,
                    onDismissRequest = { priorityExpanded = false }
                ) {
                    priorityOptions.forEach { (key, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                priorityKey = key
                                priorityExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.saveTask(
                        title = title,
                        description = description.ifBlank { null },
                        dueDateMillis = dueDateMillis,
                        priorityKey = priorityKey
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Task")
            }
        }
    }
}