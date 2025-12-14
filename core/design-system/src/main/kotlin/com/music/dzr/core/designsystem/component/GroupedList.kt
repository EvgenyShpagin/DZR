package com.music.dzr.core.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@Composable
fun GroupedList(
    modifier: Modifier = Modifier,
    shape: Shape = GroupedListDefaults.listShape,
    strokeThickness: Dp = GroupedListDefaults.StrokeThickness,
    strokeColor: Color = GroupedListDefaults.strokeColor,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .clip(shape = shape)
            .border(
                width = strokeThickness,
                color = strokeColor,
                shape = shape
            ),
        content = content
    )
}

@Composable
fun GroupedListItem(
    leadingText: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingText: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = GroupedListDefaults.itemShape,
    dividerThickness: Dp = GroupedListDefaults.StrokeThickness,
    colors: GroupedListItemColors = GroupedListDefaults.itemColors(),
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        GroupedListItemContainer(
            shape = shape,
            containerColor = colors.containerColor,
            enabled = enabled,
            onClick = onClick
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = ItemHorizontalPadding, vertical = ItemVerticalPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    CompositionLocalProvider(
                        value = LocalContentColor provides colors.leadingIconColor(enabled),
                        content = leadingIcon
                    )
                    Spacer(Modifier.width(ItemInnerPadding))
                }
                CompositionLocalProvider(
                    LocalContentColor provides colors.leadingTextColor(enabled),
                    LocalTextStyle provides MaterialTheme.typography.bodyLarge,
                    content = leadingText
                )
                Spacer(Modifier.weight(1f))
                if (trailingText != null) {
                    Spacer(Modifier.width(ItemInnerPadding))
                    CompositionLocalProvider(
                        LocalContentColor provides colors.trailingTextColor(enabled),
                        LocalTextStyle provides MaterialTheme.typography.bodyLarge,
                        content = trailingText
                    )
                }
                if (trailingIcon != null) {
                    Spacer(Modifier.width(ItemInnerPadding))
                    CompositionLocalProvider(
                        value = LocalContentColor provides colors.trailingIconColor(enabled),
                        content = trailingIcon
                    )
                }
            }
            HorizontalDivider(
                thickness = dividerThickness,
                color = colors.dividerColor
            )
        }
    }
}

@Composable
private fun GroupedListItemContainer(
    modifier: Modifier = Modifier,
    shape: Shape,
    containerColor: Color,
    onClick: (() -> Unit)?,
    enabled: Boolean,
    content: @Composable () -> Unit
) {
    val mergedModifier = Modifier
        .semantics(mergeDescendants = true) {}
        .then(modifier)

    if (onClick != null) {
        Surface(
            modifier = mergedModifier.heightIn(min = ClickableItemContainerMinHeight),
            shape = shape,
            color = containerColor,
            contentColor = Color.Unspecified,
            enabled = enabled,
            onClick = onClick,
            content = content
        )
    } else {
        Surface(
            modifier = mergedModifier.heightIn(min = NonClickableItemContainerMinHeight),
            shape = shape,
            color = containerColor,
            contentColor = Color.Unspecified,
            content = content
        )
    }
}

@PreviewLightDark
@Composable
private fun GroupedListPreview() {
    DzrTheme {
        Surface {
            GroupedList {
                GroupedListItem(
                    leadingText = { Text("Default item") },
                    onClick = {}
                )
                GroupedListItem(
                    leadingText = { Text("With leading icon") },
                    leadingIcon = { Icon(DzrIcons.Favorite, contentDescription = null) },
                    onClick = {}
                )
                GroupedListItem(
                    leadingText = { Text("With trailing text") },
                    trailingText = { Text("Trailing text") },
                    onClick = {}
                )
                GroupedListItem(
                    leadingText = { Text("With trailing icon") },
                    trailingIcon = { Icon(DzrIcons.MoreVert, contentDescription = null) },
                    onClick = {}
                )
                GroupedListItem(
                    leadingText = { Text("Disabled") },
                    leadingIcon = { Icon(DzrIcons.PlayArrow, contentDescription = null) },
                    trailingText = { Text("Trailing") },
                    trailingIcon = { Icon(DzrIcons.MoreVert, contentDescription = null) },
                    enabled = false,
                    onClick = {}
                )
            }
        }
    }
}

object GroupedListDefaults {

    val listShape: Shape
        @Composable get() = ShapeDefaults.Medium

    val StrokeThickness = 1.dp
    val strokeColor: Color
        @Composable get() = MaterialTheme.colorScheme.outlineVariant

    val itemShape: Shape
        @Composable get() = RectangleShape

    @Composable
    fun itemColors(
        containerColor: Color = this.containerColor,
        leadingIconColor: Color = this.leadingIconColor,
        trailingIconColor: Color = this.trailingIconColor,
        leadingTextColor: Color = this.leadingTextColor,
        trailingTextColor: Color = this.trailingTextColor,
        disabledLeadingIconColor: Color = this.disabledLeadingIconColor,
        disabledTrailingIconColor: Color = this.disabledTrailingIconColor,
        disabledLeadingTextColor: Color = this.disabledLeadingTextColor,
        disabledTrailingTextColor: Color = this.disabledTrailingTextColor,
        dividerColor: Color = this.strokeColor
    ) = GroupedListItemColors(
        containerColor = containerColor,
        leadingIconColor = leadingIconColor,
        trailingIconColor = trailingIconColor,
        leadingTextColor = leadingTextColor,
        trailingTextColor = trailingTextColor,
        disabledLeadingIconColor = disabledLeadingIconColor,
        disabledTrailingIconColor = disabledTrailingIconColor,
        disabledLeadingTextColor = disabledLeadingTextColor,
        disabledTrailingTextColor = disabledTrailingTextColor,
        dividerColor = dividerColor
    )


    private val containerColor: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f)

    private val leadingIconColor: Color
        @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

    private val trailingIconColor: Color
        @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

    private val leadingTextColor: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface

    private val trailingTextColor: Color
        @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

    private val disabledLeadingIconColor: Color
        @Composable get() = leadingIconColor.copy(alpha = DISABLE_OPACITY)

    private val disabledTrailingIconColor: Color
        @Composable get() = trailingIconColor.copy(alpha = DISABLE_OPACITY)

    private val disabledLeadingTextColor: Color
        @Composable get() = leadingTextColor.copy(alpha = DISABLE_OPACITY)

    private val disabledTrailingTextColor: Color
        @Composable get() = trailingTextColor.copy(alpha = DISABLE_OPACITY)

    private const val DISABLE_OPACITY = 0.38f

}

@Immutable
class GroupedListItemColors(
    val containerColor: Color,
    val leadingIconColor: Color,
    val trailingIconColor: Color,
    val leadingTextColor: Color,
    val trailingTextColor: Color,
    val disabledLeadingIconColor: Color,
    val disabledTrailingIconColor: Color,
    val disabledLeadingTextColor: Color,
    val disabledTrailingTextColor: Color,
    val dividerColor: Color
) {
    @Stable
    internal fun leadingIconColor(enabled: Boolean): Color {
        return if (enabled) leadingIconColor else disabledLeadingIconColor
    }

    @Stable
    internal fun trailingIconColor(enabled: Boolean): Color {
        return if (enabled) trailingIconColor else disabledTrailingIconColor
    }

    @Stable
    internal fun leadingTextColor(enabled: Boolean): Color {
        return if (enabled) leadingTextColor else disabledLeadingTextColor
    }

    @Stable
    internal fun trailingTextColor(enabled: Boolean): Color {
        return if (enabled) trailingTextColor else disabledTrailingTextColor
    }
}

private val ItemVerticalPadding = 8.dp
private val ItemHorizontalPadding = 16.dp
private val ItemInnerPadding = 16.dp

private val ClickableItemContainerMinHeight
    @Composable get() = LocalMinimumInteractiveComponentSize.current

private val NonClickableItemContainerMinHeight = 40.dp