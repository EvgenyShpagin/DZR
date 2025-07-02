package com.music.dzr.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@Composable
fun SectionHeader(
    @StringRes labelRes: Int,
    modifier: Modifier = Modifier
) {
    SectionHeaderInternal(
        labelRes = labelRes,
        onClick = {},
        clickable = false,
        modifier = modifier
    )
}

@Composable
fun ClickableSectionHeader(
    @StringRes labelRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SectionHeaderInternal(
        labelRes = labelRes,
        onClick = onClick,
        clickable = true,
        modifier = modifier
    )
}

@Composable
private fun SectionHeaderInternal(
    @StringRes labelRes: Int,
    onClick: () -> Unit,
    clickable: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = SectionHeaderDefaults.Shape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
        onClick = onClick,
        modifier = modifier
            .padding(vertical = 10.dp)
            .height(28.dp)
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(labelRes),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            if (clickable) {
                Icon(
                    DzrIcons.KeyboardArrowRight,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = stringResource(R.string.core_ui_cd_to_detailed_view),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ClickableSectionHeaderPreview() {
    DzrTheme {
        Surface(color = MaterialTheme.colorScheme.primaryContainer) {
            ClickableSectionHeader(
                onClick = {},
                labelRes = android.R.string.untitled
            )
        }
    }
}

@Preview
@Composable
private fun SectionHeaderPreview() {
    DzrTheme {
        Surface(color = MaterialTheme.colorScheme.primaryContainer) {
            SectionHeader(labelRes = android.R.string.untitled)
        }
    }
}

object SectionHeaderDefaults {
    val Shape = ShapeDefaults.ExtraSmall
}