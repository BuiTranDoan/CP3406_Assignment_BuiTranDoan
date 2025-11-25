package com.studyhelpers.app.ui.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyhelpers.app.data.local.entity.TaskEntity
import com.studyhelpers.app.data.local.entity.TaskPriority
import com.studyhelpers.app.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repo: TaskRepository
) : ViewModel() {

    fun saveTask(
        title: String,
        description: String?,
        dueDateMillis: Long?,
        priorityKey: String
    ) {
        viewModelScope.launch {
            repo.upsertTask(
                TaskEntity(
                    title = title,
                    description = description,
                    dueDate = dueDateMillis,
                    priority = priorityKey
                )
            )
        }
    }
}