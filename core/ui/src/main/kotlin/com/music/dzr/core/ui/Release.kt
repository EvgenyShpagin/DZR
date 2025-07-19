package com.music.dzr.core.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.model.album.ReleaseType

@Composable
fun ReleaseCard(
    title: String,
    coverUrl: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    releaseYear: String,
    explicit: Boolean,
    releaseType: ReleaseType,
    modifier: Modifier = Modifier,
    coverModifier: Modifier = Modifier
) {
    MediaGridItem(
        modifier = modifier
            .clip(ShapeDefaults.Small)
            .width(IntrinsicSize.Min),
        image = {
            TrackListCover(
                coverUrl = coverUrl,
                contentDescription = stringResource(R.string.core_ui_cd_release_cover),
                modifier = coverModifier.size(128.dp)
            )
        },
        headlineContent = {
            SingleLineText(text = title)
        },
        supportingContent = {
            Text(
                text = formatReleaseDetails(
                    context = LocalContext.current,
                    releaseYear = releaseYear,
                    explicit = explicit,
                    releaseType = releaseType
                )
            )
        },
        onClick = onClick,
        onLongClick = onLongClick,
        onLongClickLabel = stringResource(R.string.core_ui_cd_show_more)
    )
}

@Composable
fun ReleaseRow(
    title: String,
    contributors: List<String>,
    coverUrl: String,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    releaseYear: String,
    explicit: Boolean,
    releaseType: ReleaseType,
    modifier: Modifier = Modifier,
    coverModifier: Modifier = Modifier,
    onLongClick: () -> Unit = onMoreClick
) {
    MediaListItem(
        modifier = modifier.clip(ShapeDefaults.Small),
        image = {
            TrackListCover(
                coverUrl = coverUrl,
                contentDescription = stringResource(R.string.core_ui_cd_release_cover),
                modifier = coverModifier.size(96.dp)
            )
        },
        headlineContent = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        supportingContent = {
            Column {
                SingleLineText(
                    text = formatContributors(contributors),
                )
                SingleLineText(
                    text = formatReleaseDetails(
                        context = LocalContext.current,
                        releaseYear = releaseYear,
                        explicit = explicit,
                        releaseType = releaseType
                    )
                )
            }
        },
        icon = {
            IconButton(onClick = onMoreClick) {
                Icon(
                    DzrIcons.MoreVert,
                    contentDescription = stringResource(R.string.core_ui_cd_show_more)
                )
            }
        },
        onClick = onClick,
        onLongClick = onLongClick,
        onLongClickLabel = stringResource(R.string.core_ui_cd_show_more)
    )
}

@Composable
private fun SingleLineText(text: String) {
    Text(
        text = text,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

private fun formatReleaseDetails(
    context: Context,
    releaseYear: String,
    explicit: Boolean,
    releaseType: ReleaseType
): String {
    return "$releaseYear • ${if (explicit) "🅴" else ""} ${releaseType.toString(context)}"
}

@Preview
@Composable
private fun ReleaseCardPreview() {
    DzrTheme {
        ReleaseCard(
            title = "2000s Metal",
            coverUrl = "",
            onClick = {},
            onLongClick = {},
            releaseYear = "2000",
            explicit = true,
            releaseType = ReleaseType.ALBUM
        )
    }
}

@Preview
@Composable
private fun ReleaseRowPreview() {
    DzrTheme {
        ReleaseRow(
            title = "2000s Metal",
            coverUrl = "",
            contributors = listOf("Limp Bizkit", "Lil Wayne"),
            onClick = {},
            onMoreClick = {},
            releaseYear = "2000",
            explicit = true,
            releaseType = ReleaseType.ALBUM
        )
    }
}