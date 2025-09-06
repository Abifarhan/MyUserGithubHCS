package com.abidev.domain.detail.usecase

import com.abidev.domain.UserRepository
import com.abidev.domain.home.usecase.UserDomainModel
import javax.inject.Inject

class GetUserDetailUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetUserDetailUseCase {
    override suspend fun execute(username: String): UserDomainModel {
        return userRepository.getUserDetail(username)
    }
}