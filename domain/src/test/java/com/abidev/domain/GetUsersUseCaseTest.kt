package com.abidev.domain

import com.abidev.domain.home.usecase.GetUsersUseCase
import com.abidev.domain.home.usecase.GetUsersUseCaseImpl
import com.abidev.domain.home.usecase.UserDomainModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetUsersUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: GetUsersUseCase

    @Before
    fun setUp() {
        userRepository = mockk()
        useCase = GetUsersUseCaseImpl(userRepository)
    }

    @Test
    fun `execute returns search results from repository`() = runTest {
        val query = "abidev"
        val expected = listOf(
            UserDomainModel(
                id = 1,
                username = "abidev",
                avatarUrl = "https://...",
                bio = null,
                name = null,
                publicRepos = 5,
                followers = 10,
                following = 5
            )
        )
        coEvery { userRepository.searchUsers(query) } returns expected

        val result = useCase.execute(query)

        assertEquals(expected, result)
    }

    @Test
    fun `execute returns empty list for empty query`() = runTest {
        coEvery { userRepository.searchUsers("") } returns emptyList()

        val result = useCase.execute("")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `execute propagates exception from repository`() = runTest {
        val query = "abidev"
        coEvery { userRepository.searchUsers(query) } throws IOException("Network error")

        assertFailsWith<IOException> {
            useCase.execute(query)
        }
    }
}