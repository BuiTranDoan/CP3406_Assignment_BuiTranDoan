package com.studyhelpers.app.domain.repository

import com.studyhelpers.app.data.local.entity.UserEntity

interface AuthRepository {
    suspend fun register(username: String, password: String, displayName: String): Result<UserEntity>
    suspend fun login(username: String, password: String): Result<UserEntity>
}