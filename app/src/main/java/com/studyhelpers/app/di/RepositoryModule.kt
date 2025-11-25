package com.studyhelpers.app.di

import com.studyhelpers.app.data.local.dao.TaskDao
import com.studyhelpers.app.data.repository.TaskRepositoryImpl
import com.studyhelpers.app.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository =
        TaskRepositoryImpl(taskDao)
}