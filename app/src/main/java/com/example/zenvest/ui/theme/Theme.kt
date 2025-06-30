package com.example.zenvest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)
private val AppColorScheme = lightColorScheme(
    primary = AppColors.AccentPurple,
    secondary = AppColors.AccentGreen,
    tertiary = AppColors.AccentRed,
    background = AppColors.Background,
    surface = AppColors.CardBackground,
    onPrimary = AppColors.TextPrimary,
    onSecondary = AppColors.TextSecondary,
    onTertiary = AppColors.TextTertiary,
    onBackground = AppColors.TextPrimary,
    onSurface = AppColors.TextPrimary
)

@Composable
fun ZenVestTheme(
   // darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
   // dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
object AppColors {
    val Background = androidx.compose.ui.graphics.Color(0xFF0F0F1F)
    val CardBackground = androidx.compose.ui.graphics.Color(0xFF1C1C2E)
    val AccentGreen = androidx.compose.ui.graphics.Color(0xFF00FF66)
    val AccentRed = androidx.compose.ui.graphics.Color(0xFFFF3D3D)
    val AccentPurple = androidx.compose.ui.graphics.Color(0xFF7C3AED)
    val TextPrimary = androidx.compose.ui.graphics.Color.White
    val TextSecondary = androidx.compose.ui.graphics.Color(0xFFBBBBBB)
    val TextTertiary = androidx.compose.ui.graphics.Color(0xFF6B7280)
    val ChartBackground = androidx.compose.ui.graphics.Color(0xFF0B0C1C)
    val Divider = androidx.compose.ui.graphics.Color(0xFF3B3B5B)
}


