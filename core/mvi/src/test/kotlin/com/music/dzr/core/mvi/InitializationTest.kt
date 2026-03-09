package com.music.dzr.core.mvi

import com.music.dzr.core.testing.coroutine.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class InitializationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher = StandardTestDispatcher())

    @Test
    fun constructor_throwsException_whenEventsCapacityIsZero() {
        // Arrange
        val capacity = 0

        // Act & Assert
        assertFailsWith<IllegalArgumentException> {
            TestViewModel(eventsCapacity = capacity)
        }
    }

    @Test
    fun constructor_throwsException_whenEffectsCapacityIsZero() {
        // Arrange
        val capacity = 0

        // Act & Assert
        assertFailsWith<IllegalArgumentException> {
            TestViewModel(effectsCapacity = capacity)
        }
    }
}
