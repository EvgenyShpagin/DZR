package com.music.dzr.core.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.dzr.core.designsystem.theme.DzrTheme
import com.music.dzr.core.model.ReleaseType

@Composable
fun ReleaseCard(
    title: String,
    coverUrl: String,
    onClick: () -> Unit,
    releaseYear: String,
    explicit: Boolean,
    releaseType: ReleaseType,
    modifier: Modifier = Modifier,
    coverModifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(ShapeDefaults.Small)
            .clickable(
                role = Role.Button,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TrackListCover(
            coverUrl = coverUrl,
            contentDescription = stringResource(R.string.cd_release_cover),
            modifier = coverModifier.size(128.dp)
        )
        Spacer(Modifier.height(4.dp))
        ReleaseTitle(
            title = title,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        ReleaseSecondaryText(
            text = formatReleaseDetails(
                context = LocalContext.current,
                releaseYear = releaseYear,
                explicit = explicit,
                releaseType = releaseType
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun ReleaseTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
private fun ReleaseSecondaryText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = modifier
    )
}

@Preview
@Composable
private fun ReleaseCardPreview() {
    DzrTheme {
        ReleaseCard(
            title = "2000s Metal",
            coverUrl = "",
            onClick = {},
            releaseYear = "2000",
            explicit = true,
            releaseType = ReleaseType.ALBUM,
            coverModifier = Modifier.background(Color.Gray)
        )
    }
}

private fun formatReleaseDetails(
    context: Context,
    releaseYear: String,
    explicit: Boolean,
    releaseType: ReleaseType
): String {
    return "$releaseYear • ${if (explicit) "🅴" else ""} ${releaseType.toString(context)}"
}

private fun ReleaseType.toString(context: Context): String {
    return when (this) {
        ReleaseType.ALBUM -> context.getString(R.string.release_type_album)
        ReleaseType.EP -> context.getString(R.string.release_type_ep)
        ReleaseType.SINGLE -> context.getString(R.string.release_type_single)
        ReleaseType.COMPILATION -> context.getString(R.string.release_type_compilation)
        ReleaseType.FEATURED_IN -> context.getString(R.string.release_type_featured_in)
    }
}
