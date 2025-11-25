package com.studyhelpers.app.ui.edittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyhelpers.app.data.local.entity.TaskEntity
import com.studyhelpers.app.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditTaskUiState(
    val task: TaskEntity? = null
)

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val repo: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditTaskUiState())
    val uiState: StateFlow<EditTaskUiState> = _uiState

    private val taskId: Int = checkNotNull(savedStateHandle["taskId"])

    init {
        viewModelScope.launch {
            val loaded = repo.getTaskById(taskId)
            _uiState.value = EditTaskUiState(task = loaded)
        }
    }

    fun saveChanges(title: String, description: String?) {
        val current = _uiState.value.task ?: return
        viewModelScope.launch {
            repo.upsertTask(
                current.copy(
                    title = title,
                    description = description
                )
            )
        }
    }
}