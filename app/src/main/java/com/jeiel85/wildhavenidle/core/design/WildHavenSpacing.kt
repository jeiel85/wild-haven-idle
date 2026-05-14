package com.jeiel85.wildhavenidle.core.design

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 본작 공통 spacing 토큰 (4dp 그리드 기반).
 *
 * 머티리얼은 spacing 토큰을 직접 노출하지 않으므로 [LocalSpacing]
 * CompositionLocal로 주입해 사용한다. 호출부는 `WildHavenTheme.spacing.lg`
 * 형태로 접근.
 *
 * 이름은 *T-shirt* 사이즈로, 의미가 아닌 척도만 표현한다. 의미가 굳어지면
 * 별도 alias 토큰을 추가한다 (예: `cardPadding = lg`).
 */
data class WildHavenSpacing(
    val xs: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
    val xl: Dp,
    val xxl: Dp,
) {
    /** 보호구역 카드 안쪽 기본 패딩. 의미 별칭. */
    val cardPadding: Dp get() = lg

    /** 카드와 카드 사이 기본 간격. */
    val cardGap: Dp get() = lg

    /** 화면 가장자리 기본 패딩. */
    val screenPadding: Dp get() = xl
}

internal val WildHavenSpacingDefault = WildHavenSpacing(
    xs = 4.dp,
    sm = 8.dp,
    md = 12.dp,
    lg = 16.dp,
    xl = 20.dp,
    xxl = 24.dp,
)

internal val LocalSpacing = compositionLocalOf { WildHavenSpacingDefault }
