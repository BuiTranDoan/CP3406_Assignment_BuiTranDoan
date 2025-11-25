package com.studyhelpers.app.data.local.di

import android.content.Context
import androidx.room.Room
import com.studyhelpers.app.data.local.dao.TaskDao
import com.studyhelpers.app.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.studyhelpers.app.data.local.dao.UserDao
import com.studyhelpers.app.data.repository.AuthRepositoryImpl
import com.studyhelpers.app.domain.repository.AuthRepository

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context
    ): AppDatabase =
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "studyhelpers.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideAuthRepository(userDao: UserDao): AuthRepository =
        AuthRepositoryImpl(userDao)
}