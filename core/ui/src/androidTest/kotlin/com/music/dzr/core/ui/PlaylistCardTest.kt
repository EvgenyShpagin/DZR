package com.music.dzr.core.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class PlaylistCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockOnClick = mockk<() -> Unit>()
    private val name = "2000s Metal"
    private val url = "https://example.com/playlist.jpg"

    @BeforeTest
    fun setUp() {
        every { mockOnClick() } just runs

        composeTestRule.setContent {
            PlaylistCard(
                name = name,
                pictureUrl = url,
                onClick = mockOnClick,
                onLongClick = {},
                modifier = Modifier.testTag("playlistCard")
            )
        }
    }

    @Test
    fun playlistCard_displaysCorrectName() {
        composeTestRule
            .onNodeWithText(name)
            .assertIsDisplayed()
    }

    @Test
    fun playlistCard_invokesCallback_onClick() {
        composeTestRule
            .onNodeWithTag("playlistCard")
            .performClick()
        verify { mockOnClick() }
    }
}
