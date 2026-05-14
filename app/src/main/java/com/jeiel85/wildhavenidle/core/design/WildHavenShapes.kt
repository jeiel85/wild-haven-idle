package com.jeiel85.wildhavenidle.core.design

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * 머티리얼 [Shapes]에 본작의 라운딩 토큰을 매핑한다.
 *
 * 호출부는 `MaterialTheme.shapes.medium` 형태로 사용해 호환성을 유지한다.
 * 의미 → 값 매핑:
 * - extraSmall = 4.dp  (작은 칩, 인디케이터)
 * - small      = 8.dp  (보조 컨테이너)
 * - medium     = 12.dp (대부분의 카드/버튼 컨테이너 — 본작의 기본 라운딩)
 * - large      = 16.dp (헤더, 강조 카드)
 * - extraLarge = 20.dp (다이얼로그/오버레이)
 */
internal val WildHavenShapes: Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(20.dp),
)
