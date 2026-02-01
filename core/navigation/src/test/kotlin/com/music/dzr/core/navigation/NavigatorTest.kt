package com.music.dzr.core.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NavigatorTest {

    @Test
    fun goTo_appendsDestination_whenDestinationDiffersFromCurrent() {
        // Arrange
        val navigator = Navigator("Start")

        // Act
        navigator.goTo("Second")

        // Assert
        assertEquals(2, navigator.snapshot().size)
        assertEquals("Start", navigator.snapshot()[0])
        assertEquals("Second", navigator.snapshot()[1])
    }

    @Test
    fun goTo_noops_whenDestinationEqualsCurrent() {
        // Arrange
        val navigator = Navigator("Start")

        // Act
        navigator.goTo("Start")

        // Assert
        assertEquals(1, navigator.snapshot().size)
        assertEquals("Start", navigator.snapshot()[0])
    }

    @Test
    fun goTo_replacesCurrent_whenUpdateIfSameChangedEnabled_andDestinationKindMatches() {
        // Arrange
        data class Screen(val id: Int)

        val navigator = Navigator(Screen(1))

        // Act
        navigator.goTo(Screen(2), updateIfSameChanged = true)

        // Assert
        assertEquals(1, navigator.snapshot().size)
        assertEquals(Screen(2), navigator.snapshot()[0])
    }

    @Test
    fun goTo_appendsDestination_whenUpdateIfSameChangedEnabled_butDestinationKindDiffers() {
        // Arrange
        val navigator = Navigator("Start")

        // Act: "Start" is String, 1 is Int
        navigator.goTo(1, updateIfSameChanged = true)

        // Assert
        assertEquals(2, navigator.snapshot().size)
        assertEquals("Start", navigator.snapshot()[0])
        assertEquals(1, navigator.snapshot()[1])
    }

    @Test
    fun goTo_appendsDestination_whenUpdateIfSameChangedDisabled_evenIfDestinationKindMatches() {
        // Arrange
        val navigator = Navigator("Start")

        // Act
        navigator.goTo("Second", updateIfSameChanged = false)

        // Assert
        assertEquals(2, navigator.snapshot().size)
        assertEquals("Start", navigator.snapshot()[0])
        assertEquals("Second", navigator.snapshot()[1])
    }

    @Test
    fun goBack_popsCurrent_whenBackStackHasMoreThanOneEntry() {
        // Arrange
        val navigator = Navigator("Start")

        // Act
        navigator.goTo("Second")
        navigator.goBack()

        // Assert
        assertEquals(1, navigator.snapshot().size)
        assertEquals("Start", navigator.snapshot()[0])
    }

    @Test
    fun goBack_noops_whenBackStackHasSingleEntry() {
        // Arrange
        val navigator = Navigator("Start")

        // Act
        navigator.goBack()

        // Assert
        assertEquals(1, navigator.snapshot().size)
        assertEquals("Start", navigator.snapshot()[0])
    }

    @Test
    fun canGoBack_returnsFalse_whenBackStackHasSingleEntry() {
        // Arrange
        val navigator = Navigator("Start")

        // Assert
        assertFalse(navigator.canGoBack())
    }

    @Test
    fun canGoBack_returnsTrue_whenBackStackHasMoreThanOneEntry() {
        // Arrange
        val navigator = Navigator("Start")

        // Act
        navigator.goTo("Second")

        // Assert
        assertTrue(navigator.canGoBack())
    }

    @Test
    fun goBackToInstance_popsToMatchingDestination_whenDestinationExistsInBackStack() {
        // Arrange
        val navigator = Navigator("1")

        // Act
        navigator.goTo("2")
        navigator.goTo("3")
        navigator.goTo("4")
        navigator.goBackTo("2")

        // Assert
        assertEquals(2, navigator.snapshot().size)
        assertEquals("1", navigator.snapshot()[0])
        assertEquals("2", navigator.snapshot()[1])
    }

    @Test
    fun goBackToType_popsToMostRecentMatchingDestination_whenTypeExistsInBackStack() {
        // Arrange
        val navigator = Navigator("1")
        navigator.goTo("2")
        navigator.goTo("3")
        navigator.goTo("4")

        // Act: add different types so popping is observable
        navigator.goTo(5) // Int
        navigator.goTo(6) // Int
        // stack: "1", "2", "3", "4", 5, 6
        navigator.goBackTo<String>()
        // Pops to most recent String: "4"

        // Assert
        assertEquals(4, navigator.snapshot().size)
        assertEquals("4", navigator.snapshot().last())
    }

    @Test
    fun goBackToInstance_noops_whenDestinationNotFoundInBackStack() {
        // Arrange
        val navigator = Navigator("Start")

        // Act
        navigator.goTo("Second")
        navigator.goBackTo("NonExistent")

        // Assert
        assertEquals(2, navigator.snapshot().size)
    }

    @Test
    fun goBackToInstance_noops_whenDestinationIsAlreadyCurrent() {
        // Arrange
        val navigator = Navigator("Start")

        // Act
        navigator.goTo("Second")
        navigator.goBackTo("Second")

        // Assert
        assertEquals(2, navigator.snapshot().size)
    }
}
