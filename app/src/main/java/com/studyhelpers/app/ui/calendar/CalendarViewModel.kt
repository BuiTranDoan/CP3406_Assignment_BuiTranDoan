package com.studyhelpers.app.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyhelpers.app.data.local.entity.TaskEntity
import com.studyhelpers.app.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class CalendarDay(
    val dayOfMonth: Int,
    val timeMillis: Long,
    val hasTasks: Boolean
)

data class CalendarUiState(
    val monthTitle: String = "",
    val days: List<CalendarDay> = emptyList(),
    val leadingEmptyCells: Int = 0,   // how many blanks before day 1
    val selectedDate: Long? = null,
    val tasksForSelected: List<TaskEntity> = emptyList(),
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH) // 0 = Jan
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repo: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private var allTasks: List<TaskEntity> = emptyList()

    init {
        viewModelScope.launch {
            repo.getAllTasks().collect { tasks ->
                allTasks = tasks
                rebuildMonth(_uiState.value.currentYear, _uiState.value.currentMonth)
            }
        }
    }

    fun onPrevMonth() {
        val cal = Calendar.getInstance().apply {
            set(_uiState.value.currentYear, _uiState.value.currentMonth, 1)
            add(Calendar.MONTH, -1)
        }
        rebuildMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
    }

    fun onNextMonth() {
        val cal = Calendar.getInstance().apply {
            set(_uiState.value.currentYear, _uiState.value.currentMonth, 1)
            add(Calendar.MONTH, 1)
        }
        rebuildMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
    }

    fun onDaySelected(timeMillis: Long) {
        val tasksForDay = allTasks.filter { task ->
            task.dueDate?.let { isSameDay(it, timeMillis) } == true
        }
        _uiState.value = _uiState.value.copy(
            selectedDate = timeMillis,
            tasksForSelected = tasksForDay
        )
    }

    private fun rebuildMonth(year: Int, month: Int) {
        val cal = Calendar.getInstance().apply {
            set(year, month, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // 1 = Sunday
        val leadingEmpty = (firstDayOfWeek - Calendar.SUNDAY).coerceAtLeast(0)

        val days = buildList {
            for (day in 1..maxDay) {
                cal.set(Calendar.DAY_OF_MONTH, day)
                val millis = cal.timeInMillis
                val hasTasks = allTasks.any { task ->
                    task.dueDate?.let { isSameDay(it, millis) } == true
                }
                add(CalendarDay(dayOfMonth = day, timeMillis = millis, hasTasks = hasTasks))
            }
        }

        val monthTitle = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            .format(Date(cal.timeInMillis))

        // default selected date = today if in this month; else null
        val todayCal = Calendar.getInstance()
        val selected = if (
            todayCal.get(Calendar.YEAR) == year &&
            todayCal.get(Calendar.MONTH) == month
        ) todayCal.timeInMillis else null

        val tasksForSelected = selected?.let { sel ->
            allTasks.filter { it.dueDate?.let { d -> isSameDay(d, sel) } == true }
        } ?: emptyList()

        _uiState.value = CalendarUiState(
            monthTitle = monthTitle,
            days = days,
            leadingEmptyCells = leadingEmpty,
            selectedDate = selected,
            tasksForSelected = tasksForSelected,
            currentYear = year,
            currentMonth = month
        )
    }

    private fun isSameDay(millis1: Long, millis2: Long): Boolean {
        val c1 = Calendar.getInstance().apply { timeInMillis = millis1 }
        val c2 = Calendar.getInstance().apply { timeInMillis = millis2 }
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }
}
