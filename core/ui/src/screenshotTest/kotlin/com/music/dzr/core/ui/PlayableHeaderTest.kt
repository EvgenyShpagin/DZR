package com.music.dzr.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
private fun PlayableHeader_Paused_WithButton_Preview() {
    DzrTheme {
        Surface {
            PlayableHeader(
                title = "Hits",
                onPlayClick = {},
                isPlaying = false,
                content = { layout ->
                    Text(
                        text = "Released 2023",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }
}

@PreviewTest
@PreviewLightDark
@Composable
private fun PlayableHeader_Paused_WithSubtitle_Preview() {
    DzrTheme {
        Surface {
            PlayableHeader(
                title = "Greatest Hits",
                subtitle = "The Beatles",
                onPlayClick = {},
                isPlaying = false,
                content = { layout ->
                    Column {
                        Text(
                            text = "12 songs • 48 min",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    }
}

@PreviewTest
@Preview
@Composable
private fun PlayableHeader_Paused_LongTitle_Preview() {
    DzrTheme {
        Surface {
            PlayableHeader(
                title = "The Very Best of Greatest Hits Collection",
                subtitle = "The Red Hot Chili Peppers",
                onPlayClick = {},
                isPlaying = false,
                content = { layout ->
                    Text(
                        text = "25 songs • 1 hr 52 min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }
}

@PreviewTest
@Preview
@Composable
private fun PlayableHeader_Playing_ComplexContent_Preview() {
    DzrTheme {
        Surface {
            PlayableHeader(
                title = "Greatest Hits",
                subtitle = "The Beatles",
                onPlayClick = {},
                isPlaying = true,
                content = { layout ->
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                DzrIcons.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Album • 2023",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "12 songs • 48 min",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (layout.isButtonDisplayed) {
                            Spacer(modifier = Modifier.height(layout.innerSpaceHeight))
                        }
                    }
                }
            )
        }
    }
}

@PreviewTest
@Preview
@Composable
private fun PlayableHeader_Paused_NoSubtitle_Preview() {
    DzrTheme {
        Surface {
            PlayableHeader(
                title = "My Favorites",
                onPlayClick = {},
                isPlaying = false,
                content = { layout ->
                    Text(
                        text = "Created by you • 43 songs",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }
}

@PreviewTest
@Preview(device = "spec:width=320dp,height=568dp,dpi=320")
@Composable
private fun PlayableHeader_Paused_SmallScreen_Preview() {
    DzrTheme {
        Surface {
            PlayableHeader(
                title = "Greatest Hits",
                subtitle = "The Beatles",
                onPlayClick = {},
                isPlaying = false,
                content = { layout ->
                    // Test how it handles small screens where button might not fit
                    Text(
                        text = if (layout.isButtonDisplayed) "Button visible" else "Button hidden",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }
}
