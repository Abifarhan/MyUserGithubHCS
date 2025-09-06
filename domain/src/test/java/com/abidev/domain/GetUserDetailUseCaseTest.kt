package com.abidev.domain

import com.abidev.domain.detail.usecase.GetUserDetailUseCase
import com.abidev.domain.detail.usecase.GetUserDetailUseCaseImpl
import com.abidev.domain.home.usecase.UserDomainModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertFailsWith

class GetUserDetailUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: GetUserDetailUseCase

    @Before
    fun setUp() {
        userRepository = mockk()
        useCase = GetUserDetailUseCaseImpl(userRepository)
    }

    @Test
    fun `execute returns user from repository`() = runTest {
        val expected = UserDomainModel(
            id = 1,
            username = "octocat",
            avatarUrl = "https://...",
            bio = null,
            name = null,
            publicRepos = 0,
            followers = 0,
            following = 0
        )
        coEvery { userRepository.getUserDetail("octocat") } returns expected

        val result = useCase.execute("octocat")

        assertEquals(expected, result)
    }


    @Test
    fun `execute propagates exception from repository`() = runTest {
        coEvery { userRepository.getUserDetail("octocat") } throws IOException("No internet")

        assertFailsWith<IOException> {
            useCase.execute("octocat")
        }
    }
}