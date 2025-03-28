package com.fd.fakestore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fd.fakestore.data.model.User
import com.fd.fakestore.data.repository.user.IUserRepository
import com.fd.fakestore.ui.profile.ProfileState
import com.fd.fakestore.ui.profile.ProfileViewModel
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
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var userRepository: IUserRepository

    private lateinit var viewModel: ProfileViewModel

    private val testUser = User(1, "Test User", "test@example.com", "123 Main St")

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfileViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUserDetail should update state to Success on successful user retrieval`() = runTest {
        // Given
        `when`(userRepository.getUserData()).thenReturn(Result.success(testUser))

        // When
        viewModel.getUserDetail()
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is ProfileState.Success)
        assertEquals(testUser, (viewModel.state.value as ProfileState.Success).user)
    }

    @Test
    fun `getUserDetail should update state to Error when user is null`() = runTest {
        // Given
        `when`(userRepository.getUserData()).thenReturn(Result.failure(RuntimeException("User not found")))

        // When
        viewModel.getUserDetail()
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is ProfileState.Error)
        assertEquals("User not found", (viewModel.state.value as ProfileState.Error).message)
    }

    @Test
    fun `getUserDetail should update state to Error on failed user retrieval`() = runTest {
        // Given
        val errorMessage = "Failed to load user"
        `when`(userRepository.getUserData()).thenReturn(Result.failure(RuntimeException(errorMessage)))

        // When
        viewModel.getUserDetail()
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is ProfileState.Error)
        assertEquals(errorMessage, (viewModel.state.value as ProfileState.Error).message)
    }

    @Test
    fun `clearUserData should call userRepository clearUserData`() = runTest {
        // When
        viewModel.clearUserData()
        advanceUntilIdle()

        // Then
        verify(userRepository).clearUserData()
    }
}