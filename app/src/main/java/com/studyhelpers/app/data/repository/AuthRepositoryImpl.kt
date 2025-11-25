package com.studyhelpers.app.data.repository

import com.studyhelpers.app.data.local.dao.UserDao
import com.studyhelpers.app.data.local.entity.UserEntity
import com.studyhelpers.app.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun register(
        username: String,
        password: String,
        displayName: String
    ): Result<UserEntity> {
        return try {
            val existing = userDao.findByUsername(username)
            if (existing != null) {
                Result.failure(IllegalArgumentException("Username already exists"))
            } else {
                val user = UserEntity(
                    username = username,
                    password = password,
                    displayName = displayName
                )
                userDao.insertUser(user)
                val stored = userDao.findByUsername(username)!!
                Result.success(stored)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(username: String, password: String): Result<UserEntity> {
        return try {
            val user = userDao.login(username, password)
            if (user == null) {
                Result.failure(IllegalArgumentException("Invalid username or password"))
            } else {
                Result.success(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}