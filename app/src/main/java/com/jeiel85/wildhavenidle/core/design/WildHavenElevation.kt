package com.jeiel85.wildhavenidle.core.design

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 표면(Surface) tonalElevation 토큰.
 *
 * 머티리얼이 elevation 자체를 의미 토큰으로 노출하지 않으므로, 본작은 다음
 * 4단계로 통일한다.
 *
 * - none = 0.dp (평면)
 * - sm   = 1.dp (옅은 분리, 다음 해금 카드 등)
 * - md   = 2.dp (기본 카드)
 * - lg   = 4.dp (다이얼로그/오버레이)
 */
data class WildHavenElevation(
    val none: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
)

internal val WildHavenElevationDefault = WildHavenElevation(
    none = 0.dp,
    sm = 1.dp,
    md = 2.dp,
    lg = 4.dp,
)

internal val LocalElevation = compositionLocalOf { WildHavenElevationDefault }
