package com.raite.studyroom.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = Teal,
    onPrimary = CardLight,
    primaryContainer = TealTint,
    onPrimaryContainer = TealDeep,
    secondary = TealDeep,
    onSecondary = CardLight,
    secondaryContainer = TealTint,
    onSecondaryContainer = TealDeep,
    tertiary = Orange,
    onTertiary = CardLight,
    tertiaryContainer = OrangeContainerLight,
    onTertiaryContainer = OrangeOnDark,
    background = NeutralLight,
    onBackground = TextLight,
    surface = CardLight,
    onSurface = TextLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    error = ErrorLight,
    onError = CardLight,
)

private val DarkColors = darkColorScheme(
    primary = TealDark,
    onPrimary = TealDeep,
    primaryContainer = TealContainerDark,
    onPrimaryContainer = TealTint,
    secondary = TealDark,
    onSecondary = TealContainerDark,
    secondaryContainer = TealContainerDark,
    onSecondaryContainer = TealTint,
    tertiary = OrangeDark,
    onTertiary = OrangeOnDark,
    tertiaryContainer = OrangeContainerDark,
    onTertiaryContainer = OrangeContainerLight,
    background = NeutralDark,
    onBackground = TextDark,
    surface = CardDark,
    onSurface = TextDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    error = ErrorDark,
    onError = OnErrorDark,
)

/**
 * App theme.
 *
 * NOTE: dynamic color is intentionally NOT used (spec section 5.3) so the demo
 * looks identical on every phone. Light/dark follows the caller (system setting
 * or the user's DataStore preference).
 */
@Composable
fun StudyRoomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // No-op from API 35 on (edge-to-edge is enforced there); kept so older
            // devices still paint the status bar to match the theme background.
            @Suppress("DEPRECATION")
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = StudyRoomTypography,
        shapes = StudyRoomShapes,
        content = content,
    )
}
