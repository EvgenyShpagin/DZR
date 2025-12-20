package com.music.dzr.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.component.ThemeAndFontScalePreviews
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@ThemeAndFontScalePreviews
@Composable
private fun MediaGridItem_Enabled_Preview() {
    DzrTheme {
        Surface {
            MediaGridItem(
                headlineContent = {
                    Text(
                        "Awesome Album (Off)",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                image = {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentScale = ContentScale.Crop
                    )
                },
                supportingContent = {
                    Text(
                        "The Best Artist",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                enabled = true,
                onClick = {}
            )
        }
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun MediaGridItem_Disabled_Preview() {
    DzrTheme {
        Surface {
            MediaGridItem(
                headlineContent = {
                    Text(
                        "Awesome Album (Off)",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                image = {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentScale = ContentScale.Crop
                    )
                },
                supportingContent = {
                    Text(
                        "The Best Artist",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
private fun MediaGridItem_Enabled_NoSupporting_Preview() {
    DzrTheme {
        Surface {
            MediaGridItem(
                headlineContent = {
                    Text(
                        "Podcast Episode",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                image = {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_slideshow),
                        contentDescription = "Episode Art",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentScale = ContentScale.Crop
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
private fun MediaGridItem_Enabled_LongHeadline_Preview() {
    DzrTheme {
        Surface {
            MediaGridItem(
                headlineContent = {
                    Text(
                        "A Very Long Title That Will Surely Be Ellipsized",
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                image = {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_mapmode),
                        contentDescription = "Art",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.tertiaryContainer),
                        contentScale = ContentScale.Crop
                    )
                },
                supportingContent = {
                    Text(
                        "Artist With A Fairly Long Name Also",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                enabled = true,
                onClick = {}
            )
        }
    }
}
