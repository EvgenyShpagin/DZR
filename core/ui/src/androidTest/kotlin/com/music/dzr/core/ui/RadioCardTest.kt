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

class RadioCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockOnClick = mockk<() -> Unit>()
    private val name = "Summer Afternoon"
    private val url = "https://example.com/radio.jpg"

    @BeforeTest
    fun setUp() {
        every { mockOnClick() } just runs

        composeTestRule.setContent {
            RadioCard(
                name = name,
                pictureUrl = url,
                onClick = mockOnClick,
                modifier = Modifier.testTag("radioCard")
            )
        }
    }

    @Test
    fun radioCard_displaysName() {
        composeTestRule.onNodeWithText(name).assertIsDisplayed()
    }

    @Test
    fun radioCard_invokesCallback_onClick() {
        composeTestRule.onNodeWithTag("radioCard").performClick()
        verify { mockOnClick() }
    }
}