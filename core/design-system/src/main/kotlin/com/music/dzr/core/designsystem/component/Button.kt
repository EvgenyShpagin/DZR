package com.music.dzr.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@Composable
private fun Modifier.buttonSize() = this
    .heightIn(min = DzrButtonDefaults.ContainerHeight)

@Composable
fun DzrButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.buttonSize(),
        enabled = enabled,
        shape = DzrButtonDefaults.Shape,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun DzrButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    DzrButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = if (leadingIcon != null) {
            ButtonDefaults.ButtonWithIconContentPadding
        } else {
            ButtonDefaults.ContentPadding
        },
    ) {
        DzrButtonContent(
            text = text,
            leadingIcon = leadingIcon,
        )
    }
}

@Composable
fun DzrOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.buttonSize(),
        enabled = enabled,
        shape = DzrButtonDefaults.Shape,
        contentPadding = contentPadding,
        content = content,
    )
}

@PreviewLightDark
@Composable
private fun DzrButtonPreview() {
    DzrTheme {
        DzrButton(onClick = {}) {
            Text("Filled button")
        }
    }
}

@PreviewLightDark
@Composable
private fun DzrButtonWithLeadingIconPreview() {
    DzrTheme {
        DzrButton(
            onClick = {},
            text = {
                Text("Filled button")
            },
            leadingIcon = {
                Icon(DzrIcons.FavoriteBorder, null)
            }
        )
    }
}

@Composable
fun DzrOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    DzrOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = if (leadingIcon != null) {
            ButtonDefaults.ButtonWithIconContentPadding
        } else {
            ButtonDefaults.ContentPadding
        },
    ) {
        DzrButtonContent(
            text = text,
            leadingIcon = leadingIcon,
        )
    }
}

@PreviewLightDark
@Composable
private fun DzrOutlinedButtonPreview() {
    DzrTheme {
        Surface {
            DzrOutlinedButton(onClick = {}) {
                Text("Outlined button")
            }
        }
    }
}

@Composable
private fun DzrButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    if (leadingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
            leadingIcon()
        }
    }

    Box(
        Modifier.padding(
            start = if (leadingIcon != null) {
                ButtonDefaults.IconSpacing
            } else {
                0.dp
            }
        )
    ) {
        text()
    }
}


object DzrButtonDefaults {
    val Shape = ShapeDefaults.Medium

    // Default size for 14.sp text
    val ContainerHeight = 36.dp
}