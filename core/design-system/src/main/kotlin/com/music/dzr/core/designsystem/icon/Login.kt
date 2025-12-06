package com.music.dzr.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val LoginBold: ImageVector
    get() {
        if (_loginBold != null) return _loginBold!!

        _loginBold = ImageVector.Builder(
            name = "DzrIcons.LoginMedium",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(475.48f, 859.22f)
                verticalLineToRelative(-106f)
                horizontalLineToRelative(277.74f)
                verticalLineToRelative(-546.44f)
                horizontalLineTo(475.48f)
                verticalLineToRelative(-106f)
                horizontalLineToRelative(383.74f)
                verticalLineToRelative(758.44f)
                horizontalLineTo(475.48f)
                close()
                moveToRelative(-97.52f, -152.09f)
                lineToRelative(-73.66f, -75.52f)
                lineTo(402.91f, 533f)
                horizontalLineTo(100.78f)
                verticalLineToRelative(-106f)
                horizontalLineToRelative(302.13f)
                lineToRelative(-98.61f, -98.61f)
                lineToRelative(73.66f, -75.52f)
                lineTo(604.52f, 480f)
                lineTo(377.96f, 707.13f)
                close()

            }
        }.build()

        return _loginBold!!
    }

private var _loginBold: ImageVector? = null
