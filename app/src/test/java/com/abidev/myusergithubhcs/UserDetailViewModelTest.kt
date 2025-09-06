package com.abidev.myusergithubhcs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.abidev.core.Resource
import com.abidev.domain.detail.usecase.GetUserDetailUseCase
import com.abidev.domain.home.usecase.UserDomainModel
import com.abidev.myusergithubhcs.presenter.detail.UserDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.text.contains

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getUserDetailUseCase: GetUserDetailUseCase
    private lateinit var viewModel: UserDetailViewModel
    private lateinit var observer: Observer<Resource<UserDomainModel>>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getUserDetailUseCase = mockk()

        observer = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUserDetail emits success when useCase returns data`() = runTest {
        // Given
        val username = "abidev"
        val expectedUser = UserDomainModel(
            id = 1,
            username = "abidev",
            avatarUrl = "https://...",
            bio = "Android Developer",
            name = "Abi Dev",
            publicRepos = 10,
            followers = 100,
            following = 50
        )
        coEvery { getUserDetailUseCase.execute(username) } returns expectedUser

        // When
        viewModel = UserDetailViewModel(getUserDetailUseCase)
        viewModel.uiState.observeForever(observer)
        viewModel.getUserDetail(username)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is Resource.Success)
        assertEquals(expectedUser, (viewModel.uiState.value as Resource.Success).data)
        viewModel.uiState.removeObserver(observer)
    }

    @Test
    fun `getUserDetail emits error when useCase throws exception`() = runTest {
        // Given
        val username = "abidev"
        coEvery { getUserDetailUseCase.execute(username) } throws kotlin.Exception("Network error")

        // When
        viewModel = UserDetailViewModel(getUserDetailUseCase)
        viewModel.uiState.observeForever(observer)
        viewModel.getUserDetail(username)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is Resource.Error)
        val message = (viewModel.uiState.value as Resource.Error).message
        assertTrue(message.contains("Network error") || message == "Gagal memuat detail user")
        viewModel.uiState.removeObserver(observer)
    }
}