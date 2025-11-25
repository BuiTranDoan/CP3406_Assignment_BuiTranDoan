package com.studyhelpers.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyhelpers.app.data.local.entity.TaskEntity
import com.studyhelpers.app.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.studyhelpers.app.data.remote.QuoteApi
data class HomeUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val isLoading: Boolean = false,
    val quote: String? = null,
    val quoteAuthor: String? = null,
    val hideCompleted: Boolean = false,
    val showDeleteDialog: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: TaskRepository,
    private val quoteApi: QuoteApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
        loadQuote()
    }
    fun onTaskCheckedChanged(task: TaskEntity, isCompleted: Boolean) {
        viewModelScope.launch {
            val updated = task.copy(isCompleted = isCompleted)
            repo.upsertTask(updated)
        }
    }

    fun onTaskDeleteClicked(task: TaskEntity) {
        viewModelScope.launch {
            repo.deleteTask(task)
        }
    }
    private fun loadQuote() {
        viewModelScope.launch {
            try {
                val result = quoteApi.getRandomQuote()
                _uiState.value = _uiState.value.copy(
                    quote = result.content,
                    quoteAuthor = result.author
                )
            } catch (e: Exception) {

            }
        }
    }
    private fun loadTasks() {
        viewModelScope.launch {
            repo.getAllTasks().collect { tasks ->
                val sorted = tasks.sortedWith(
                    compareBy<com.studyhelpers.app.data.local.entity.TaskEntity> { it.dueDate ?: Long.MAX_VALUE }
                        .thenByDescending { it.priority } // MAJOR > NORMAL > MINOR
                )
                _uiState.value = _uiState.value.copy(tasks = sorted)
            }
        }
    }
        fun setHideCompleted(hide: Boolean) {
            _uiState.value = _uiState.value.copy(hideCompleted = hide)
        }
    var pendingDeleteTask: TaskEntity? = null
        private set

    fun requestDelete(task: TaskEntity) {
        pendingDeleteTask = task
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
    }

    fun dismissDeleteDialog() {
        pendingDeleteTask = null
        _uiState.value = _uiState.value.copy(showDeleteDialog = false)
    }

    fun confirmDelete() {
        viewModelScope.launch {
            pendingDeleteTask?.let { repo.deleteTask(it) }
            pendingDeleteTask = null
            _uiState.value = _uiState.value.copy(showDeleteDialog = false)
        }
    }

}
