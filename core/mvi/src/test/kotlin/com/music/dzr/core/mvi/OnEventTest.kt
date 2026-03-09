package com.music.dzr.core.mvi

import com.music.dzr.core.testing.coroutine.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class OnEventTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher = StandardTestDispatcher())

    @Test
    fun onEvent_continuesProcessing_afterException() = runTest {
        // Arrange
        val error = RuntimeException("test error")
        val vm = TestViewModel()
        vm.onHandle = { event ->
            if (event == TestEvent.Click) throw error
        }

        // Act
        vm.onEvent(TestEvent.Click)
        vm.onEvent(TestEvent.Input("after"))
        advanceUntilIdle()

        // Assert
        assertEquals(listOf<TestEvent>(TestEvent.Input("after")), vm.handledEvents)
        assertEquals(1, vm.handlingErrors.size)
        assertEquals(TestEvent.Click, vm.handlingErrors[0].first)
        assertEquals(error, vm.handlingErrors[0].second)
    }

    @Test
    fun onEvent_dropsEvents_whenCapacityIsFull() = runTest {
        // Arrange
        val vm = TestViewModel(eventsCapacity = 1)

        // Act
        vm.onEvent(TestEvent.Click)
        vm.onEvent(TestEvent.Input("overflow"))

        // Assert
        assertEquals(1, vm.droppedEvents.size)
        assertEquals(TestEvent.Input("overflow"), vm.droppedEvents[0].first)
    }

    // Immediate events

    @Test
    fun onEvent_handlesEvent_whenSingle() = runTest {
        // Arrange
        val vm = TestViewModel()

        // Act
        vm.onEvent(TestEvent.Click)
        advanceUntilIdle()

        // Assert
        assertEquals(listOf<TestEvent>(TestEvent.Click), vm.handledEvents)
    }

    @Test
    fun onEvent_handlesAllInOrder_whenMultiple() = runTest {
        // Arrange
        val vm = TestViewModel()

        // Act
        vm.onEvent(TestEvent.Click)
        vm.onEvent(TestEvent.Input("hello"))
        vm.onEvent(TestEvent.Click)
        advanceUntilIdle()

        // Assert
        assertEquals(
            listOf(TestEvent.Click, TestEvent.Input("hello"), TestEvent.Click),
            vm.handledEvents
        )
    }

    // Debounce events

    @Test
    fun onEvent_firesEvent_afterTimeout() = runTest {
        // Arrange
        val vm = TestViewModel()

        // Act
        vm.onEvent(TestEvent.DebouncedSearch("hello"))
        advanceTimeBy(TestEvent.TIMEOUT_MS + 1)
        advanceUntilIdle()

        // Assert
        assertEquals(1, vm.handledEvents.size)
        assertEquals(TestEvent.DebouncedSearch("hello"), vm.handledEvents[0])
    }

    @Test
    fun onEvent_doesNotFireEvent_beforeTimeout() = runTest {
        // Arrange
        val vm = TestViewModel()

        // Act
        vm.onEvent(TestEvent.DebouncedSearch("hello"))
        advanceTimeBy(TestEvent.TIMEOUT_MS / 2)

        // Assert
        assertEquals(0, vm.handledEvents.size)
    }

    @Test
    fun onEvent_firesLastEvent_onRapidEvents() = runTest {
        // Arrange
        val vm = TestViewModel()

        // Act
        vm.onEvent(TestEvent.DebouncedSearch("h"))
        advanceTimeBy(TestEvent.TIMEOUT_MS / 2)
        vm.onEvent(TestEvent.DebouncedSearch("he"))
        advanceTimeBy(TestEvent.TIMEOUT_MS / 2)
        vm.onEvent(TestEvent.DebouncedSearch("hel"))
        advanceTimeBy(TestEvent.TIMEOUT_MS + 1)
        advanceUntilIdle()

        // Assert
        assertEquals(1, vm.handledEvents.size)
        assertEquals(TestEvent.DebouncedSearch("hel"), vm.handledEvents[0])
    }

    @Test
    fun onEvent_firesAllIndependent_afterTimeout() = runTest {
        // Arrange
        val vm = TestViewModel()

        // Act
        vm.onEvent(TestEvent.DebouncedNav("a"))
        vm.onEvent(TestEvent.DebouncedInput("b"))
        advanceTimeBy(TestEvent.TIMEOUT_MS + 1)
        advanceUntilIdle()

        // Assert
        assertEquals(2, vm.handledEvents.size)
    }

    // Throttle events

    @Test
    fun onEvent_passesEvent_whenSingleEvent() = runTest {
        // Arrange
        val fakeTime = 0L
        val vm = TestViewModel(elapsedRealtime = { fakeTime })
        advanceUntilIdle()

        // Act
        vm.onEvent(TestEvent.ThrottledClick)
        advanceUntilIdle()

        // Assert
        assertEquals(1, vm.handledEvents.size)
    }

    @Test
    fun onEvent_dropsEvent_withinThrottleWindow() = runTest {
        // Arrange
        var fakeTime = 0L
        val vm = TestViewModel(elapsedRealtime = { fakeTime })

        // Act
        vm.onEvent(TestEvent.ThrottledClick)
        advanceUntilIdle()
        fakeTime = TestEvent.WINDOW_MS / 2
        vm.onEvent(TestEvent.ThrottledClick)
        advanceUntilIdle()

        // Assert
        assertEquals(1, vm.handledEvents.size)
    }

    @Test
    fun onEvent_passesEvent_afterWindow() = runTest {
        // Arrange
        var fakeTime = 0L
        val vm = TestViewModel(elapsedRealtime = { fakeTime })

        // Act
        vm.onEvent(TestEvent.ThrottledClick)
        advanceUntilIdle()
        fakeTime = TestEvent.WINDOW_MS * 2
        vm.onEvent(TestEvent.ThrottledClick)
        advanceUntilIdle()

        // Assert
        assertEquals(2, vm.handledEvents.size)
    }

    @Test
    fun onEvent_passesEvents_whenDifferentScopes() = runTest {
        // Arrange
        val fakeTime = 0L
        val vm = TestViewModel(elapsedRealtime = { fakeTime })

        // Act
        vm.onEvent(TestEvent.NavThrottle)
        vm.onEvent(TestEvent.MutationThrottle)
        advanceUntilIdle()

        // Assert
        assertEquals(2, vm.handledEvents.size)
        assertEquals(TestEvent.NavThrottle, vm.handledEvents[0])
        assertEquals(TestEvent.MutationThrottle, vm.handledEvents[1])
    }
}
