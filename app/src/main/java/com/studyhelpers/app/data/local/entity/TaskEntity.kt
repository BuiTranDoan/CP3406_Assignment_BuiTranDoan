package com.studyhelpers.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String = "",
    val description: String? = null,
    val category: String = "",
    val dueDate: Long? = null,
    val priority: String = "NORMAL",
    val isCompleted: Boolean = false
)