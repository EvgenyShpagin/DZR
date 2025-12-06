package com.music.dzr.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.music.dzr.core.designsystem.R


private val Oswald = FontFamily(
    Font(R.font.oswald_extra_light, FontWeight.ExtraLight),
    Font(R.font.oswald_light, FontWeight.Light),
    Font(R.font.oswald_regular, FontWeight.Normal),
    Font(R.font.oswald_medium, FontWeight.Medium),
    Font(R.font.oswald_semi_bold, FontWeight.SemiBold),
    Font(R.font.oswald_bold, FontWeight.Bold)
)

private val baseline = Typography()

internal val DzrTypography = Typography(
    displayLarge = baseline.displayLarge.copy(
        fontFamily = Oswald,
        fontWeight = FontWeight.Bold
    ),
    displayMedium = baseline.displayMedium.copy(
        fontFamily = Oswald,
        fontWeight = FontWeight.Medium
    ),
    displaySmall = baseline.displaySmall.copy(fontFamily = Oswald),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = Oswald),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = Oswald),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = Oswald),
    titleLarge = baseline.titleLarge.copy(fontFamily = Oswald),
    titleMedium = baseline.titleMedium.copy(fontFamily = Oswald, fontWeight = FontWeight.Medium),
    titleSmall = baseline.titleSmall.copy(fontFamily = Oswald),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = Oswald),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = Oswald),
    bodySmall = baseline.bodySmall.copy(fontFamily = Oswald),
    labelLarge = baseline.labelLarge.copy(fontFamily = Oswald),
    labelMedium = baseline.labelMedium.copy(fontFamily = Oswald),
    labelSmall = baseline.labelSmall.copy(fontFamily = Oswald),
)
