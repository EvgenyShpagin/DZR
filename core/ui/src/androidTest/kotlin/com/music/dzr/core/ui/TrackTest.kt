package com.music.dzr.core.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class TrackTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val mockOnClick = mockk<() -> Unit>()
    private val mockOnMoreClick = mockk<() -> Unit>()
    private val mockOnLongClick = mockk<() -> Unit>()

    private val title = "Sample Track"
    private val coverUrl = "https://example.com/track.jpg"
    private val contributors = listOf("Artist 1", "Artist 2")

    @BeforeTest
    fun setUp() {
        every { mockOnClick() } just runs
        every { mockOnMoreClick() } just runs
        every { mockOnLongClick() } just runs
    }

    @Test
    fun track_displaysContent() {
        setContent()
        composeTestRule
            .onNode(hasText(title, substring = true))
            .assertIsDisplayed()
        val formattedContributors = formatContributors(contributors)
        composeTestRule
            .onNodeWithText(formattedContributors)
            .assertIsDisplayed()
    }

    @Test
    fun track_displaysExplicitMarker_whenExplicit() {
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

    @Test
    fun track_invokesCallback_onClick() {
        setContent()
        composeTestRule.onNodeWithTag("track").performClick()
        verify { mockOnClick() }
    }

    @Test
    fun track_invokesCallback_onLongClick() {
        setContent()
        composeTestRule
            .onNodeWithTag("track")
            .performTouchInput { longClick() }
        verify { mockOnLongClick() }
    }

    @Test
    fun track_invokesCallback_onMoreButtonClick() {
        setContent()
        val moreButtonDesc = composeTestRule.activity.getString(R.string.core_ui_cd_show_more)
        composeTestRule
            .onNodeWithContentDescription(moreButtonDesc)
            .performClick()
        verify { mockOnMoreClick() }
    }

    private fun setContent(explicit: Boolean = true) {
        composeTestRule.setContent {
            Track(
                coverUrl = coverUrl,
                onClick = mockOnClick,
                title = title,
                explicit = explicit,
                contributors = contributors,
                onMoreClick = mockOnMoreClick,
                onLongClick = mockOnLongClick,
                modifier = Modifier.testTag("track")
            )
        }
    }
}