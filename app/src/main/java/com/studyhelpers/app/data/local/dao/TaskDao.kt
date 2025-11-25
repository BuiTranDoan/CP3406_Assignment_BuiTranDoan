package com.studyhelpers.app.data.local.dao

import androidx.room.*
import com.studyhelpers.app.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY dueDate IS NULL, dueDate ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTask(task: TaskEntity): Long

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE date(dueDate / 1000, 'unixepoch') = date(:dayStart / 1000, 'unixepoch')")
    fun getTasksForDay(dayStart: Long): Flow<List<TaskEntity>>
}