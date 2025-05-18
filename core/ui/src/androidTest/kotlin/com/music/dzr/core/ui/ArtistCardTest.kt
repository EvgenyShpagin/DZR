package com.music.dzr.core.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ArtistCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockOnClick = mockk<() -> Unit>()
    private val mockOnLongClick = mockk<() -> Unit>()

    private val testArtistName = "The Rolling Stones"
    private val testPictureUrl = "https://example.com/artist.jpg"

    @Before
    fun setUp() {
        every { mockOnClick() } just runs
        every { mockOnLongClick() } just runs
        composeTestRule.setContent {
            ArtistCard(
                name = testArtistName,
                pictureUrl = testPictureUrl,
                onClick = mockOnClick,
                onLongClick = mockOnLongClick,
                modifier = Modifier.testTag("artistCard")
            )
        }
    }

    @Test
    fun artistCard_displaysCorrectName() {
        composeTestRule
            .onNodeWithText(testArtistName)
            .assertIsDisplayed()
    }

    @Test
    fun artistCard_invokesCallback_onClick() {
        composeTestRule
            .onNodeWithTag("artistCard")
            .performClick()
        verify { mockOnClick() }
    }

    @Test
    fun artistCard_invokesCallback_onLongClick() {
        composeTestRule
            .onNodeWithTag("artistCard")
            .performTouchInput { longClick() }
        verify { mockOnLongClick() }
    }
}