package com.music.dzr.core.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.music.dzr.core.designsystem.theme.DzrTheme

@PreviewTest
@PreviewLightDark
@Composable
private fun DzrLoadingIndicator_CompactSize_Preview() {
    DzrTheme {
        Surface {
            DzrLoadingIndicator()
        }
    }
}

@PreviewTest
@Composable
private fun DzrLoadingIndicator_ExpandedSize_Preview() {
    DzrTheme {
        Surface {
            DzrLoadingIndicator(modifier = Modifier.size(DzrLoadingIndicatorDefaults.expandedSize))
        }
    }
}

@PreviewTest
@Composable
private fun DzrLoadingIndicator_ExtraBars_CompactSize_Preview() {
    DzrTheme {
        Surface {
            DzrLoadingIndicator(
                modifier = Modifier.size(DzrLoadingIndicatorDefaults.expandedSize),
                barFractions =
                    listOf(0.15f, 0.5f, 0.7f, 0.80f, 0.90f, 1.00f, 0.90f, 0.80f, 0.7f, 0.5f, 0.15f)
            )
        }
    }
}
