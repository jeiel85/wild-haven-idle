package com.jeiel85.wildhavenidle.core.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/**
 * Wild Haven Idle 테마 진입점.
 *
 * 머티리얼3 토큰(ColorScheme, Typography, Shapes)과 본작 고유 토큰
 * (의미 색상, spacing, elevation, 텍스트 스타일)을 함께 주입한다.
 *
 * 호출부에서는 다음 두 가지 경로로 토큰에 접근한다.
 * - 머티리얼 호환: `MaterialTheme.colorScheme.primary`, `MaterialTheme.shapes.medium`
 * - 본작 고유:    `WildHavenTheme.colors.rarityRare`, `WildHavenTheme.spacing.lg`
 */
@Composable
fun WildHavenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val materialColorScheme = if (darkTheme) WildHavenDarkColorScheme else WildHavenLightColorScheme
    val haveColors = if (darkTheme) WildHavenDarkColors else WildHavenLightColors

    CompositionLocalProvider(
        LocalWildHavenColors provides haveColors,
        LocalWildHavenTextStyles provides WildHavenLightTextStyles,
        LocalSpacing provides WildHavenSpacingDefault,
        LocalElevation provides WildHavenElevationDefault,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = WildHavenTypography,
            shapes = WildHavenShapes,
            content = content,
        )
    }
}

/**
 * 토큰 접근자. 함수 [WildHavenTheme]와 같은 이름의 object — Kotlin은 함수와
 * 클래시파이어를 다른 네임스페이스로 보므로 공존 가능하다 (머티리얼의
 * `MaterialTheme`과 동일한 패턴).
 */
object WildHavenTheme {
    val colors: WildHavenColors
        @Composable
        @ReadOnlyComposable
        get() = LocalWildHavenColors.current

    val textStyles: WildHavenTextStyles
        @Composable
        @ReadOnlyComposable
        get() = LocalWildHavenTextStyles.current

    val spacing: WildHavenSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current

    val elevation: WildHavenElevation
        @Composable
        @ReadOnlyComposable
        get() = LocalElevation.current
}
