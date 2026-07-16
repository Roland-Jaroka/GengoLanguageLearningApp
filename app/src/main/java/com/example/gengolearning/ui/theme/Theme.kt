package com.example.gengolearning.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    primary = BgBlue,
    onPrimary = White,
    secondary = Blue,
    onSecondary = White,
    secondaryContainer = TransparentBlue,
    tertiary = Pink40,
    background = White,
    onBackground = PandaBlack,
    surface = White,
    onSurface = PandaBlack
)

private val SunsetCoralColorScheme = lightColorScheme(
    primary = CoralPrimary,
    onPrimary = White,
    secondary = PeachSecondary,
    onSecondary = White,
    secondaryContainer = PeachSecondaryTransparent,
    tertiary = GoldAccent,
    background = SoftCream,
    onBackground = DarkBrown,
    surface = SoftCream,
    onSurface = PandaBlack
)

private val MidnightTealColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = White,
    secondary = AquaSecondary,
    onSecondary = White,
    secondaryContainer = AquaSecondaryTransparent,
    tertiary = CyanAccent,
    background = MistBackground,
    onBackground = DarkTealText,
    surface = SoftTealSurface,
    onSurface = PandaBlack
)

private val AutumnAmberColorScheme = lightColorScheme(
    primary = AmberPrimary,
    onPrimary = White,
    secondary = HoneySecondary,
    onSecondary = White,
    secondaryContainer = HoneySecondaryTransparent,
    tertiary = CaramelAccent,
    background = WarmBackground,
    onBackground = DarkCoffeeText,
    surface = SoftAmberSurface,
    onSurface = PandaBlack
)

@Composable
fun MyLanguageLearningAppTheme(

    dynamicColor: Boolean = true,
    appColorTheme: AppColorTheme = AppColorTheme.BASIC,
    content: @Composable () -> Unit
) {
    val colorScheme = when (appColorTheme) {
        AppColorTheme.BASIC -> LightColorScheme
        AppColorTheme.SUNSET -> SunsetCoralColorScheme
        AppColorTheme.MIDNIGHT_TEAL -> MidnightTealColorScheme
        AppColorTheme.Autumn -> AutumnAmberColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}