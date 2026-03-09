package com.music.dzr.core.mvi

import com.music.dzr.core.testing.coroutine.MainDispatcherRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TriggerEffectTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher = StandardTestDispatcher())

    @Test
    fun triggerEffect_emitsEffect_whenEnoughCapacity() = runTest {
        // Arrange
        val vm = TestViewModel()
        val effects = mutableListOf<TestEffect>()
        val uiScope = CoroutineScope(mainDispatcherRule.testDispatcher)
        uiScope.launch { vm.uiEffect.collect { effects += it } }

        // Act
        vm.emitEffect(TestEffect.Navigate)
        advanceUntilIdle()

        // Assert
        assertEquals(listOf<TestEffect>(TestEffect.Navigate), effects)
    }

    @Test
    fun triggerEffect_emitsEffectsInOrder_whenEnoughCapacity() = runTest {
        // Arrange
        val vm = TestViewModel()
        val effects = mutableListOf<TestEffect>()
        val uiScope = CoroutineScope(mainDispatcherRule.testDispatcher)
        uiScope.launch { vm.uiEffect.collect { effects += it } }

        // Act
        vm.emitEffect(TestEffect.Navigate)
        vm.emitEffect(TestEffect.ShowToast)
        advanceUntilIdle()

        // Assert
        assertEquals(listOf(TestEffect.Navigate, TestEffect.ShowToast), effects)
    }

    @Test
    fun triggerEffect_dropsEffects_whenCapacityIsFull() = runTest {
        // Arrange
        val vm = TestViewModel(effectsCapacity = 1)

        // Act
        vm.emitEffect(TestEffect.Navigate)
        vm.emitEffect(TestEffect.ShowToast)

        // Assert
        assertEquals(1, vm.droppedEffects.size)
        assertEquals(TestEffect.ShowToast, vm.droppedEffects[0].first)
    }
}
