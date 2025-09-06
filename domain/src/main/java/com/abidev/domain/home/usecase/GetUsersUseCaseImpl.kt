package com.abidev.domain.home.usecase

import com.abidev.domain.UserRepository
import javax.inject.Inject

class GetUsersUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetUsersUseCase {
    override suspend fun execute(query: String): List<UserDomainModel> {
        return userRepository.searchUsers(query)
    }
}