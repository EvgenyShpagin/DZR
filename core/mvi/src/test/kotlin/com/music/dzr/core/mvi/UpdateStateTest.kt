package com.music.dzr.core.mvi

import com.music.dzr.core.testing.coroutine.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateStateTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher = StandardTestDispatcher())

    @Test
    fun uiState_emitsInitialState_onInit() = runTest {
        // Arrange
        val vm = TestViewModel()

        // Act
        val uiState = vm.uiState.value

        // Assert
        assertEquals(TestState(value = 0), uiState)
    }

    @Test
    fun updateState_changesState_whenCalled() = runTest {
        // Arrange
        val vm = TestViewModel()

        // Act
        vm.setState { it.copy(value = 42) }

        // Assert
        assertEquals(TestState(value = 42), vm.uiState.value)
    }

    @Test
    fun updateState_changesStateSequentially_whenCalledMultipleTimes() = runTest {
        // Arrange
        val vm = TestViewModel()

        // Act
        vm.setState { it.copy(value = it.value + 10) }
        vm.setState { it.copy(value = it.value + 5) }
        vm.setState { it.copy(value = it.value * 2) }

        // Assert
        assertEquals(TestState(value = 30), vm.uiState.value)
    }
}
