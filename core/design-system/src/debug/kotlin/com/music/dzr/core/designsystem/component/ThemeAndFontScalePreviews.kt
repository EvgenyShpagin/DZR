package com.music.dzr.core.designsystem.component

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark

/**
 * Multi-preview annotation that represents various device configurations and themes
 * to ensure component visibility and layout adaptation.
 *
 * It includes:
 * - Light and Dark themes (standard state)
 * - Font scale 1.5 (accessibility)
 * - Font scale 0.85 (small text)
 *
 * Using this annotation prevents combinatorial explosion by testing these properties in isolation
 * rather than multiplying them (e.g. Dark x Font=1.5).
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@PreviewLightDark
@Preview(name = "150%", fontScale = 1.5f)
@Preview(name = "85%", fontScale = 0.85f)
annotation class ThemeAndFontScalePreviews
