package com.abidev.myusergithubhcs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.abidev.core.Resource
import com.abidev.domain.home.usecase.GetDefaultUsersUseCase
import com.abidev.domain.home.usecase.GetUsersUseCase
import com.abidev.domain.home.usecase.UserDomainModel
import com.abidev.myusergithubhcs.presenter.home.HomeViewModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var searchUsersUseCase: GetUsersUseCase
    private lateinit var getDefaultUsersUseCase: GetDefaultUsersUseCase
    private lateinit var observer: Observer<Resource<List<UserDomainModel>>>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        searchUsersUseCase = mockk()
        getDefaultUsersUseCase = mockk()

        observer = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `searchUsers emits success when useCase returns data`() = runTest {
        // Given
        val query = "abidev"
        val expected = listOf(
            UserDomainModel(
                id = 1,
                username = "abidev",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                bio = null,
                name = null,
                publicRepos = 5,
                followers = 10,
                following = 5
            )
        )
        coEvery { searchUsersUseCase.execute(query) } returns expected
        coEvery { getDefaultUsersUseCase.execute() } returns emptyList()

        // When
        val viewModel = HomeViewModel(searchUsersUseCase, getDefaultUsersUseCase)
        viewModel.uiState.observeForever(observer)
        viewModel.searchUsers(query)
        advanceUntilIdle()

        // Then
        verify(exactly = 1) { observer.onChanged(Resource.Success(expected)) }
        assertTrue(viewModel.uiState.value is Resource.Success)
        assertEquals(expected, (viewModel.uiState.value as Resource.Success).data)
        viewModel.uiState.removeObserver(observer)
    }

    @Test
    fun `searchUsers emits error when useCase throws exception`() = runTest {
        // Given
        val query = "abidev"
        coEvery { searchUsersUseCase.execute(query) } throws Exception("Network error")
        coEvery { getDefaultUsersUseCase.execute() } returns emptyList()

        // When
        val viewModel = HomeViewModel(searchUsersUseCase, getDefaultUsersUseCase)
        viewModel.uiState.observeForever(observer)
        viewModel.searchUsers(query)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is Resource.Error)
        val errorMessage = (viewModel.uiState.value as Resource.Error).message
        assertTrue(errorMessage.contains("Network error") || errorMessage == "Fail Load Data")
        verify(exactly = 1) { observer.onChanged(match { it is Resource.Error }) }
        viewModel.uiState.removeObserver(observer)
    }

    @Test
    fun `init loads default users on start`() = runTest {
        // Given
        val defaultUsers = listOf(
            UserDomainModel(
                id = 1,
                username = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/583231?v=4",
                bio = null,
                name = null,
                publicRepos = 0,
                followers = 0,
                following = 0
            )
        )
        coEvery { getDefaultUsersUseCase.execute() } returns defaultUsers

        // When
        val viewModel = HomeViewModel(searchUsersUseCase, getDefaultUsersUseCase)
        viewModel.uiState.observeForever(observer)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is Resource.Success)
        assertEquals(defaultUsers, (viewModel.uiState.value as Resource.Success).data)
        verify(exactly = 1) { observer.onChanged(match { it is Resource.Success }) }
        viewModel.uiState.removeObserver(observer)
    }

    @Test
    fun `init emits error when getDefaultUsersUseCase throws exception`() = runTest {
        // Given
        coEvery { getDefaultUsersUseCase.execute() } throws Exception("Network error")

        // When
        val viewModel = HomeViewModel(searchUsersUseCase, getDefaultUsersUseCase)
        viewModel.uiState.observeForever(observer)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is Resource.Error)
        val errorMessage = (viewModel.uiState.value as Resource.Error).message
        assertTrue(errorMessage.contains("Network error") || errorMessage == "Fail Load Data")
        verify(exactly = 1) { observer.onChanged(match { it is Resource.Error }) }
        viewModel.uiState.removeObserver(observer)
    }
}