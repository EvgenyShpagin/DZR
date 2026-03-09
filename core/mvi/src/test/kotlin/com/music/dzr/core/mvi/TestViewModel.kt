package com.music.dzr.core.mvi

/**
 * A testing implementation of [MutableStateViewModel] used for verifying the behavior
 * of the core MVI architecture components, such as event policies, state updates, and side effects.
 *
 * It provides mutable collections to inspect the history of handled events, dropped events,
 * dropped effects, and errors that occurred during event handling.
 */
internal class TestViewModel(
    eventsCapacity: Int = DEFAULT_EVENTS_CAPACITY,
    effectsCapacity: Int = DEFAULT_EFFECTS_CAPACITY,
    elapsedRealtime: () -> Long = { 0L },
) : MutableStateViewModel<TestState, TestEvent, TestEffect>(
    initialUiState = TestState(),
    eventsCapacity = eventsCapacity,
    effectsCapacity = effectsCapacity,
    elapsedRealtime = elapsedRealtime,
) {
    val handledEvents = mutableListOf<TestEvent>()
    val droppedEvents = mutableListOf<Pair<TestEvent, Throwable?>>()
    val droppedEffects = mutableListOf<Pair<TestEffect, Throwable?>>()
    val handlingErrors = mutableListOf<Pair<TestEvent, Exception>>()

    var onHandle: (suspend (TestEvent) -> Unit)? = null

    override suspend fun handleEvent(event: TestEvent) {
        onHandle?.invoke(event)
        handledEvents += event
    }

    override fun onEventDropped(event: TestEvent, exception: Throwable?) {
        droppedEvents += event to exception
    }

    override fun onEffectDropped(effect: TestEffect, exception: Throwable?) {
        droppedEffects += effect to exception
    }

    override fun onEventHandlingError(event: TestEvent, exception: Exception) {
        handlingErrors += event to exception
    }

    fun emitEffect(effect: TestEffect) = triggerEffect(effect)

    fun setState(transform: (TestState) -> TestState) = updateState(transform)
}
