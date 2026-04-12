package com.music.dzr.feature.auth.ui.screen.welcome

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WelcomeCoverMarqueeTest {

    @Test
    fun distributeIntoRows_throwsException_whenInsufficientItems() {
        // Arrange
        val items = listOf(WelcomeCoverMarqueeItemUiState(imageUrl = "url"))
        val rowCount = 2

        // Act & Assert
        assertFailsWith<IllegalArgumentException>() {
            items.distributeIntoRows(rowCount = rowCount)
        }
    }

    @Test
    fun distributeIntoRows_placesRemainingItemsAtTheEnd_whenCountIsDividedWithRemainder() {
        // Arrange
        val items = listOf(
            WelcomeCoverMarqueeItemUiState(imageUrl = "url-1"),
            WelcomeCoverMarqueeItemUiState(imageUrl = "url-2"),
            WelcomeCoverMarqueeItemUiState(imageUrl = "url-3"),
            WelcomeCoverMarqueeItemUiState(imageUrl = "url-4"),
            WelcomeCoverMarqueeItemUiState(imageUrl = "url-5"),
            WelcomeCoverMarqueeItemUiState(imageUrl = "url-6"),
            WelcomeCoverMarqueeItemUiState(imageUrl = "url-7"),
        )
        val rowCount = 3

        // Act
        val rows = items.distributeIntoRows(rowCount = rowCount)

        // Assert
        assertEquals(3, rows.count())
        assertEquals(2, rows[0].count())
        assertEquals(2, rows[1].count())
        assertEquals(3, rows[2].count())
    }
}
