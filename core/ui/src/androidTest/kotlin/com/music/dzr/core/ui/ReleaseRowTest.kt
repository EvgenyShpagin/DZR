package com.music.dzr.core.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
 * Tests for [ReleaseRow]
 */
class ReleaseRowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val mockOnClick = mockk<() -> Unit>()
    private val mockOnMoreClick = mockk<() -> Unit>()

    private val title = "2000s Metal"
    private val contributors = listOf("Limp Bizkit", "Lil Wayne")
    private val coverUrl = "https://example.com/album.jpg"
    private val releaseYear = "2000"
    private val releaseType = ReleaseType.ALBUM

    @Before
    fun setUp() {
        every { mockOnClick() } just runs
        every { mockOnMoreClick() } just runs
    }

    @Test
    fun releaseRow_displaysCorrectTitle() {
        setContent()
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun releaseRow_displaysContributors() {
        setContent()
        val formattedContributors = formatContributors(contributors)
        composeTestRule.onNodeWithText(formattedContributors).assertIsDisplayed()
    }

    @Test
    fun releaseRow_invokesCallback_onClick() {
        setContent()
        composeTestRule.onNodeWithTag("releaseRow").performClick()
        verify { mockOnClick() }
    }

    @Test
    fun releaseRow_invokesCallback_moreButton() {
        setContent()
        val moreButtonDesc = composeTestRule.activity.getString(R.string.cd_show_more)
        composeTestRule.onNodeWithContentDescription(moreButtonDesc).performClick()
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
            ReleaseRow(
                title = title,
                contributors = contributors,
                coverUrl = coverUrl,
                onClick = mockOnClick,
                onMoreClick = mockOnMoreClick,
                releaseYear = releaseYear,
                explicit = explicit,
                releaseType = releaseType,
                modifier = Modifier.testTag("releaseRow")
            )
        }
    }
}
