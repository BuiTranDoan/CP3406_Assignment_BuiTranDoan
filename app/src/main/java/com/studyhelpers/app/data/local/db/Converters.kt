package com.studyhelpers.app.data.local.db

import androidx.room.TypeConverter
import com.studyhelpers.app.data.local.entity.TaskCategory
import com.studyhelpers.app.data.local.entity.TaskPriority

class Converters {

    @TypeConverter
    fun fromCategory(category: TaskCategory?): String? = category?.name

    @TypeConverter
    fun toCategory(name: String?): TaskCategory? =
        name?.let { TaskCategory.valueOf(it) }

    @TypeConverter
    fun fromPriority(priority: TaskPriority): String = priority.name

    @TypeConverter
    fun toPriority(value: String): TaskPriority =
        TaskPriority.valueOf(value)
}
