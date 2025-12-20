package com.music.dzr.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.component.ThemeAndFontScalePreviews
import com.music.dzr.core.designsystem.icon.DzrIcons
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@ThemeAndFontScalePreviews
@Composable
private fun MediaListItem_Enabled_Preview() {
    DzrTheme {
        Surface {
            MediaListItem(
                headlineContent = { Text("The Greatest Song Ever Written In The World (Disabled)") },
                supportingContent = { Text("By The Talented Artist & Another One") },
                image = {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentScale = ContentScale.Crop
                    )
                },
                icon = {
                    Icon(
                        imageVector = DzrIcons.MoreVert,
                        contentDescription = "More options"
                    )
                },
                enabled = false,
                onClick = {}
            )
        }
    }
}


@PreviewTest
@PreviewLightDark
@Composable
private fun MediaListItem_Disabled_Preview() {
    DzrTheme {
        Surface {
            MediaListItem(
                headlineContent = { Text("The Greatest Song Ever Written In The World (Disabled)") },
                supportingContent = { Text("By The Talented Artist & Another One") },
                image = {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentScale = ContentScale.Crop
                    )
                },
                icon = {
                    Icon(
                        imageVector = DzrIcons.MoreVert,
                        contentDescription = "More options"
                    )
                },
                enabled = false,
                onClick = {}
            )
        }
    }
}

@PreviewTest
@Preview
@Composable
private fun MediaListItem_Enabled_NoImage_Preview() {
    DzrTheme {
        Surface {
            MediaListItem(
                headlineContent = { Text("Song Without Cover") },
                supportingContent = { Text("Anonymous Artist") },
                icon = {
                    Icon(
                        imageVector = DzrIcons.Favorite,
                        contentDescription = "Favorite"
                    )
                },
                enabled = true,
                onClick = {}
            )
        }
    }
}

@PreviewTest
@Preview
@Composable
private fun MediaListItem_Enabled_NoSubtitle_Preview() {
    DzrTheme {
        Surface {
            MediaListItem(
                headlineContent = { Text("Headline Only Track") },
                image = {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_slideshow),
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentScale = ContentScale.Crop
                    )
                },
                icon = {
                    Icon(
                        imageVector = DzrIcons.MoreVert,
                        contentDescription = "More options"
                    )
                },
                enabled = true,
                onClick = {}
            )
        }
    }
}

@PreviewTest
@Preview
@Composable
private fun MediaListItem_Enabled_Minimal_Preview() {
    DzrTheme {
        Surface {
            MediaListItem(
                headlineContent = { Text("Minimal Item") },
                enabled = true,
                onClick = {}
            )
        }
    }
}
