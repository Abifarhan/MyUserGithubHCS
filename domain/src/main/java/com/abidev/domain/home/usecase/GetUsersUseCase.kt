package com.abidev.domain.home.usecase

interface GetUsersUseCase {
    suspend fun execute(query: String): List<UserDomainModel>
}