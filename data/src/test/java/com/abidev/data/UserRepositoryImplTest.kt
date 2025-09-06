package com.abidev.data

import com.abidev.data.local.UserDao
import com.abidev.data.local.UserEntity
import com.abidev.data.remote.GitHubApiService
import com.abidev.data.remote.SearchResponseDto
import com.abidev.data.remote.UserDetailDto
import com.abidev.data.remote.UserDto
import com.abidev.data.repository.UserRepositoryImpl
import com.abidev.data.repository.toDomain
import com.abidev.domain.home.usecase.UserDomainModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.text.contains

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var apiService: GitHubApiService
    private lateinit var dao: UserDao
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        apiService = mockk()
        dao = mockk()

        repository = UserRepositoryImpl(apiService, dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchUsers returns from cache when available`() = runTest {
        // Given
        val query = "abidev"
        val cached = listOf(
            UserEntity(
                id = 1,
                login = "abidev",
                avatarUrl = "https://...",
                bio = null,
                name = null,
                publicRepos = 5,
                followers = 10,
                following = 5
            )
        )
        coEvery { dao.searchUsers(query) } returns flowOf(cached)

        // When
        val result = repository.searchUsers(query)

        // Then
        assert(result.size == 1)
        assert(result[0].username == "abidev")
    }

    @Test
    fun `searchUsers returns from API when cache empty`() = runTest {
        // Given
        val query = "abidev"
        coEvery { dao.searchUsers(query) } returns flowOf(emptyList())

        val responseDto = SearchResponseDto(
            items = listOf(
                UserDto(
                    id = 1,
                    login = "abidev",
                    avatarUrl = "https://..."
                )
            )
        )
        coEvery { apiService.searchUsers(query) } returns responseDto
        coEvery { dao.insertAll(any()) } just Runs

        val expected = listOf(
            UserDomainModel(
                id = 1,
                username = "abidev",
                avatarUrl = "https://...",
                bio = null,
                name = null,
                publicRepos = 0,
                followers = 0,
                following = 0
            )
        )

        // When
        val result = repository.searchUsers(query)

        // Then
        assert(result == expected)
    }

    @Test
    fun `getDefaultUsers returns from cache when not empty`() = runTest {
        // Given
        val cached = listOf(
            UserEntity(
                id = 1,
                login = "abidev",
                avatarUrl = "https://...",
                bio = null,
                name = null,
                publicRepos = 5,
                followers = 10,
                following = 5
            )
        )
        coEvery { dao.observeAllLimited(50) } returns flowOf(cached)

        // When
        val result = repository.getDefaultUsers()


        // Then
        assert(result.size == 1)
        assert(result[0].username == "abidev")
    }

    @Test
    fun `getDefaultUsers returns from API when cache empty`() = runTest {
        // Given
        coEvery { dao.observeAllLimited(50) } returns flowOf(emptyList())

        val remote = listOf(
            UserDto(
                id = 1,
                login = "octocat",
                avatarUrl = "https://..."
            )
        )
        coEvery { apiService.getUsers() } returns remote
        coEvery { dao.insertAll(any()) } just Runs

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

        // When
        val result = repository.getDefaultUsers()

        // Then
        assert(result == expected)
    }

    @Test
    fun `getUserDetail returns from API when successful`() = runTest {
        // Given
        val username = "octocat"
        val userDto = UserDetailDto(
            id = 1,
            login = "octocat",
            avatarUrl = "https://...",
            bio = "Hello",
            name = "The Octocat",
            publicRepos = 10,
            followers = 50,
            following = 10,
            htmlUrl = ""
        )
        coEvery { apiService.getUserDetail(username) } returns userDto
        coEvery { dao.insert(any()) } just Runs

        val expected = userDto.toDomain()

        // When
        val result = repository.getUserDetail(username)

        // Then
        assert(result == expected)
    }

    @Test
    fun `getUserDetail returns from cache when API fails and cache exists`() = runTest {
        // Given
        val username = "octocat"
        coEvery { apiService.getUserDetail(username) } throws IOException("No internet")

        val cached = UserEntity(
            id = 1,
            login = "octocat",
            avatarUrl = "https://...",
            bio = "Cached bio",
            name = "Cached Name",
            publicRepos = 5,
            followers = 20,
            following = 10
        )
        coEvery { dao.getUserByUsername(username) } returns cached

        val expected = cached.toDomain()

        // When
        val result = repository.getUserDetail(username)

        // Then
        assert(result == expected)
    }

    @Test
    fun `getUserDetail throws exception when API fails and cache is empty`() = runTest {
        // Given
        val username = "octocat"
        coEvery { apiService.getUserDetail(username) } throws IOException("No internet")
        coEvery { dao.getUserByUsername(username) } returns null

        // When & Then
        try {
            repository.getUserDetail(username)
            assert(false)
        } catch (e: Exception) {
            assert(e.message?.contains("Gagal mengambil data dari API dan tidak ada data lokal") == true)
        }
    }
}