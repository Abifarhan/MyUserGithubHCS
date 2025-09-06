package com.abidev.domain.home.usecase

import com.abidev.domain.UserRepository
import javax.inject.Inject

class GetDefaultUsersUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetDefaultUsersUseCase {
    override suspend fun execute(): List<UserDomainModel> {
        return userRepository.getDefaultUsers()
    }
}