package com.studyhelpers.app.domain.repository

import com.studyhelpers.app.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAllTasks(): Flow<List<TaskEntity>>

    fun getTasksForDay(dayStart: Long): Flow<List<TaskEntity>>

    suspend fun getTaskById(id: Int): TaskEntity?

    suspend fun upsertTask(task: TaskEntity)

    suspend fun deleteTask(task: TaskEntity)
}