package com.music.dzr.core.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Rule
import kotlin.test.Test

class PlayableHeaderTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val title = "Title"
    private val subtitle = "Subtitle"

    @Test
    fun playableHeader_displaysAllComponents() {
        val contentNodeTag = "content"

        composeTestRule.setContent {
            PlayableHeader(
                title = title,
                subtitle = subtitle,
                onPlayClick = {},
                isPlaying = false,
                content = { _ ->
                    Box(
                        Modifier
                            .testTag(contentNodeTag)
                            .size(10.dp)
                    )
                }
            )
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
        composeTestRule.onNodeWithText(subtitle).assertIsDisplayed()
        composeTestRule.onNodeWithTag(contentNodeTag).assertIsDisplayed()
    }

    @Test
    fun playableHeader_invokesCallback_onClick() {
        val mockOnPlayClick = mockk<() -> Unit>()
        every { mockOnPlayClick() } just runs
        composeTestRule.setContent {
            PlayableHeader(
                title = title,
                onPlayClick = mockOnPlayClick,
                isPlaying = false,
                content = {}
            )
        }

        val playMusicText = composeTestRule.activity.getString(R.string.core_ui_cd_play_music)
        composeTestRule
            .onNodeWithContentDescription(playMusicText)
            .performClick()

        verify { mockOnPlayClick() }
    }

    @Test
    fun playableHeader_showsCorrectIcon_whenPlaying() {
        composeTestRule.setContent {
            PlayableHeader(
                title = title,
                onPlayClick = {},
                isPlaying = true,
                content = {}
            )
        }

        // Check for pause button when isPlaying=true
        val pauseMusicText = composeTestRule.activity.getString(R.string.core_ui_cd_pause_music)
        composeTestRule.onNodeWithContentDescription(pauseMusicText).assertIsDisplayed()
    }
}
