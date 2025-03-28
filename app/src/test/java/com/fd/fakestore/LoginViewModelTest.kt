package com.fd.fakestore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fd.fakestore.data.model.User
import com.fd.fakestore.data.repository.user.IUserRepository
import com.fd.fakestore.ui.login.LoginState
import com.fd.fakestore.ui.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var userRepository: IUserRepository

    private lateinit var viewModel: LoginViewModel

    private val testUser = User(1, "testuser", "test@example.com", "123 Main St")

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login should update state to Success on successful login`() = runTest {
        // Given
        `when`(userRepository.login("testuser", "password")).thenReturn(Result.success(testUser))

        // When
        viewModel.login("testuser", "password")
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is LoginState.Success)
        assertEquals(testUser, (viewModel.state.value as LoginState.Success).user)
    }

    @Test
    fun `login should update state to Error on failed login`() = runTest {
        // Given
        val errorMessage = "Invalid credentials"
        `when`(userRepository.login("testuser", "wrongpassword")).thenReturn(
            Result.failure(
                RuntimeException(errorMessage)
            )
        )

        // When
        viewModel.login("testuser", "wrongpassword")
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is LoginState.Error)
        assertEquals(errorMessage, (viewModel.state.value as LoginState.Error).message)
    }

    @Test
    fun `updateState should update the state correctly`() = runTest {
        // Given
        val newState = LoginState.Loading

        // When
        viewModel.updateState(newState)
        advanceUntilIdle()

        // Then
        assertEquals(newState, viewModel.state.value)
    }

    @Test
    fun `isAuthenticated should return true when user is authenticated`() = runTest {
        // Given
        `when`(userRepository.isAuthenticated()).thenReturn(true)

        // When
        val isAuthenticated = viewModel.isAuthenticated()
        advanceUntilIdle()

        // Then
        assertTrue(isAuthenticated)
    }

    @Test
    fun `isAuthenticated should return false when user is not authenticated`() = runTest {
        // Given
        `when`(userRepository.isAuthenticated()).thenReturn(false)

        // When
        val isAuthenticated = viewModel.isAuthenticated()
        advanceUntilIdle()

        // Then
        assertEquals(false, isAuthenticated)
    }
}