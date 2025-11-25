package com.studyhelpers.app.ui.focustimer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FocusTimerUiState(
    val seconds: Int = 0,
    val isRunning: Boolean = false
)

@HiltViewModel
class FocusTimerViewModel @Inject constructor() : ViewModel() {

    private var _state = FocusTimerUiState()
    val state: FocusTimerUiState get() = _state

    private var timerJob: Job? = null

    fun start() {
        if (_state.isRunning) return

        _state = _state.copy(isRunning = true)
        timerJob = viewModelScope.launch {
            while (_state.isRunning) {
                delay(1000L)
                _state = _state.copy(seconds = _state.seconds + 1)
            }
        }
    }

    fun pause() {
        _state = _state.copy(isRunning = false)
        timerJob?.cancel()
        timerJob = null
    }

    fun reset() {
        pause()
        _state = FocusTimerUiState()
    }
}