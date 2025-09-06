package com.abidev.domain.home.usecase

interface GetDefaultUsersUseCase {
    suspend fun execute(): List<UserDomainModel>
}