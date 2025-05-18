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
import com.music.dzr.core.model.ReleaseType
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests for [ReleaseCard]
 */
class ReleaseCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockOnClick = mockk<() -> Unit>()
    private val mockOnLongClick = mockk<() -> Unit>()

    private val title = "2000s Metal"
    private val coverUrl = "https://example.com/album.jpg"
    private val releaseYear = "2000"
    private val releaseType = ReleaseType.ALBUM

    @Before
    fun setUp() {
        every { mockOnClick() } just runs
        every { mockOnLongClick() } just runs
    }

    @Test
    fun releaseCard_displaysTitle() {
        setContent()
        composeTestRule
            .onNodeWithText(title)
            .assertIsDisplayed()
    }

    @Test
    fun releaseCard_invokesCallback_onClick() {
        setContent()
        composeTestRule
            .onNodeWithTag("releaseCard")
            .performClick()
        verify { mockOnClick() }
    }

    @Test
    fun releaseCard_invokesCallback_onLongClick() {
        setContent()
        composeTestRule.onNodeWithTag("releaseCard").performTouchInput { longClick() }
        verify { mockOnLongClick() }
    }

    @Test
    fun releaseCard_displaysCorrectYear() {
        setContent()
        composeTestRule
            .onNodeWithText(releaseYear, substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun releaseCard_displaysExplicitMarker_whenExplicit() {
        setContent(explicit = true)
        composeTestRule
            .onNodeWithText("🅴", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun releaseCard_doesNotShowExplicitMarker_whenNotExplicit() {
        setContent(explicit = false)
        composeTestRule
            .onNodeWithText("🅴", substring = true)
            .assertDoesNotExist()
    }

    private fun setContent(explicit: Boolean = true) {
        composeTestRule.setContent {
            ReleaseCard(
                title = title,
                coverUrl = coverUrl,
                onClick = mockOnClick,
                onLongClick = mockOnLongClick,
                releaseYear = releaseYear,
                explicit = explicit,
                releaseType = releaseType,
                modifier = Modifier.testTag("releaseCard")
            )
        }
    }
}