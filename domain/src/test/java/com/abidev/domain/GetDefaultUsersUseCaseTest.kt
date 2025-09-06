package com.abidev.domain

import com.abidev.domain.home.usecase.GetDefaultUsersUseCase
import com.abidev.domain.home.usecase.GetDefaultUsersUseCaseImpl
import com.abidev.domain.home.usecase.UserDomainModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertFailsWith

class GetDefaultUsersUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: GetDefaultUsersUseCase

    @Before
    fun setUp() {
        userRepository = mockk()
        useCase = GetDefaultUsersUseCaseImpl(userRepository)
    }

    @Test
    fun `execute returns default users from repository`() = runTest {
        val expected = listOf(
            UserDomainModel(
                id = 1,
                username = "octocat",
                avatarUrl = "https://...",
                bio = null,
                name = null,
                publicRepos = 0,
                followers = 0,
                following = 0
            )
        )
        coEvery { userRepository.getDefaultUsers() } returns expected

        val result = useCase.execute()

        assertEquals(expected, result)
    }

    @Test
    fun `execute propagates exception from repository`() = runTest {
        coEvery { userRepository.getDefaultUsers() } throws IOException("Network error")

        assertFailsWith<IOException> {
            useCase.execute()
        }
    }
}