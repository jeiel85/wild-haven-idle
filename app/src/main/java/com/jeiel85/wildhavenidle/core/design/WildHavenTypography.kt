package com.jeiel85.wildhavenidle.core.design

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Wild Haven Idle 타이포그래피 토큰.
 *
 * 머티리얼 [Typography] 기본값을 기준으로 게임 톤에 맞춰 다음을 조정한다.
 * - 큰 숫자(보호 포인트 등): displaySmall, headlineMedium의 weight를 SemiBold로
 * - 카드 타이틀: titleMedium에 자간 약간 증가로 가독성 확보
 * - 본문: 머티리얼 기본 유지 (가독성 손해 방지)
 */
internal val WildHavenTypography: Typography = run {
    val base = Typography()
    base.copy(
        displaySmall = base.displaySmall.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.sp,
        ),
        headlineMedium = base.headlineMedium.copy(
            fontWeight = FontWeight.SemiBold,
        ),
        titleLarge = base.titleLarge.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.1.sp,
        ),
        titleMedium = base.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.1.sp,
        ),
        labelLarge = base.labelLarge.copy(
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
        ),
        labelMedium = base.labelMedium.copy(
            letterSpacing = 0.4.sp,
        ),
    )
}

/**
 * 머티리얼 토큰으로는 표현하기 어려운 게임 고유 텍스트 스타일.
 * 예: 보호 포인트 카운터처럼 *숫자 한 줄을 크게* 보여주는 스타일.
 */
data class WildHavenTextStyles(
    val statBig: TextStyle,
    val statSmall: TextStyle,
    val tagLabel: TextStyle,
)

internal val WildHavenLightTextStyles = WildHavenTextStyles(
    statBig = TextStyle(
        fontSize = 36.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.5).sp,
    ),
    statSmall = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.2.sp,
    ),
    tagLabel = TextStyle(
        fontSize = 11.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.6.sp,
    ),
)

internal val LocalWildHavenTextStyles = androidx.compose.runtime.compositionLocalOf<WildHavenTextStyles> {
    error("WildHavenTextStyles not provided. Wrap your content in WildHavenTheme.")
}
