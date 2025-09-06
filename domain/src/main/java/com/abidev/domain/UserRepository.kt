package com.abidev.domain

import com.abidev.domain.home.usecase.UserDomainModel

interface UserRepository {
    suspend fun searchUsers(query: String): List<UserDomainModel>
    suspend fun getDefaultUsers(): List<UserDomainModel>
    suspend fun getUserDetail(username: String): UserDomainModel
}