package com.music.dzr.core.designsystem.component

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class GroupedListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val itemTag = "grouped_list_item_tag"

    @Test
    fun groupedListItem_showsLeadingIcon() {
        // Arrange
        composeTestRule.setContent {
            DzrTheme {
                GroupedList {
                    GroupedListItem(
                        modifier = Modifier.testTag(itemTag),
                        leadingText = { Text("Item with icon") },
                        leadingIcon = { Icon(DzrIcons.Favorite, contentDescription = "Favorite") }
                    )
                }
            }
        }

        // Assert
        composeTestRule.onNodeWithTag(itemTag).assertIsDisplayed()
        composeTestRule.onNodeWithText("Item with icon").assertIsDisplayed()
    }

    @Test
    fun groupedListItem_showsTrailingText() {
        // Arrange
        composeTestRule.setContent {
            GroupedList {
                GroupedListItem(
                    modifier = Modifier.testTag(itemTag),
                    leadingText = { Text("Leading text") },
                    trailingText = { Text("Trailing text") }
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithTag(itemTag).assertIsDisplayed()
        composeTestRule.onNodeWithText("Leading text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Trailing text").assertIsDisplayed()
    }

    @Test
    fun groupedListItem_showsTrailingIcon() {
        // Arrange
        composeTestRule.setContent {
            GroupedList {
                GroupedListItem(
                    modifier = Modifier.testTag(itemTag),
                    leadingText = { Text("Leading text") },
                    trailingIcon = { Icon(DzrIcons.MoreVert, contentDescription = "More") }
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithTag(itemTag).assertIsDisplayed()
        composeTestRule.onNodeWithText("Leading text").assertIsDisplayed()
    }

    @Test
    fun groupedListItem_showsAllSlots() {
        // Arrange
        composeTestRule.setContent {
            GroupedList {
                GroupedListItem(
                    modifier = Modifier.testTag(itemTag),
                    leadingIcon = { Icon(DzrIcons.Favorite, contentDescription = "Favorite") },
                    leadingText = { Text("Leading text") },
                    trailingText = { Text("Trailing text") },
                    trailingIcon = { Icon(DzrIcons.MoreVert, contentDescription = "More") }
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithTag(itemTag).assertIsDisplayed()
        composeTestRule.onNodeWithText("Leading text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Trailing text").assertIsDisplayed()
    }

    @Test
    fun groupedListItem_handlesClick_whenClickable() {
        // Arrange
        val onClick = mockk<() -> Unit>()
        every { onClick() } just runs

        composeTestRule.setContent {
            GroupedListItem(
                modifier = Modifier.testTag(itemTag),
                leadingText = { Text("Clickable item") },
                onClick = onClick
            )
        }

        // Act
        composeTestRule.onNodeWithTag(itemTag).performClick()

        // Verify
        verify { onClick() }
    }

    @Test
    fun groupedListItem_ignoresClicks_whenDisabled() {
        // Arrange
        val onClick = mockk<() -> Unit>()
        every { onClick() } just runs

        composeTestRule.setContent {
            GroupedListItem(
                modifier = Modifier.testTag(itemTag),
                leadingText = { Text("Disabled item") },
                enabled = false,
                onClick = {}
            )
        }

        // Act
        composeTestRule.onNodeWithTag(itemTag).performClick()

        // Verify
        verify(exactly = 0) { onClick() }
    }

    @Test
    fun groupedList_showsAllItems() {
        // Arrange
        composeTestRule.setContent {
            GroupedList(modifier = Modifier) {
                GroupedListItem(
                    leadingText = { Text("Item 1") }
                )
                GroupedListItem(
                    leadingText = { Text("Item 2") }
                )
                GroupedListItem(
                    leadingText = { Text("Item 3") }
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Item 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Item 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Item 3").assertIsDisplayed()
    }
}
