package com.studyhelpers.app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.studyhelpers.app.data.local.dao.TaskDao
import com.studyhelpers.app.data.local.dao.UserDao
import com.studyhelpers.app.data.local.entity.TaskEntity
import com.studyhelpers.app.data.local.entity.UserEntity

@Database(
    entities = [
        TaskEntity::class,
        UserEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
}