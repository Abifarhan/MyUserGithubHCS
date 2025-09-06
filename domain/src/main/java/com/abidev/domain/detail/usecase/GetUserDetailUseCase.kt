package com.abidev.domain.detail.usecase

import com.abidev.domain.home.usecase.UserDomainModel

interface GetUserDetailUseCase {
    suspend fun execute(username: String): UserDomainModel
}