package com.studyhelpers.app.data.repository

import com.studyhelpers.app.data.local.dao.TaskDao
import com.studyhelpers.app.data.local.entity.TaskEntity
import com.studyhelpers.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<TaskEntity>> =
        dao.getAllTasks()

    override fun getTasksForDay(dayStart: Long): Flow<List<TaskEntity>> =
        dao.getTasksForDay(dayStart)

    override suspend fun getTaskById(id: Int): TaskEntity? =
        dao.getTaskById(id)

    override suspend fun upsertTask(task: TaskEntity) {
        dao.upsertTask(task)
    }

    override suspend fun deleteTask(task: TaskEntity) {
        dao.deleteTask(task)
    }
}
