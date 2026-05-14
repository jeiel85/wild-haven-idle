package com.jeiel85.wildhavenidle.core.design

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Wild Haven Idle 의미 색상 팔레트.
 *
 * 머티리얼 ColorScheme로 표현하기 어려운 게임 고유 의미(희귀도, 보호구역 톤,
 * 회복 단계 톤)는 여기서 정의해 [LocalWildHavenColors]로 주입한다.
 *
 * 이름은 "어디에 쓰는지"가 아니라 "무엇을 의미하는지"로 짓는다.
 * 화면 코드에서 직접 색상 값을 들고 다니지 말고 이 토큰만 참조한다.
 */
data class WildHavenColors(
    val sanctuaryPrimary: Color,
    val sanctuarySoft: Color,
    val sanctuaryDeep: Color,
    val skyAccent: Color,
    val earthAccent: Color,
    val rarityCommon: Color,
    val rarityUncommon: Color,
    val rarityRare: Color,
    val rarityEpic: Color,
    val rarityLegendary: Color,
)

/**
 * Light theme palette. Material `lightColorScheme`이 표현하는 컬러는 여기서
 * 동일 hex로 노출되어 호환성을 유지한다.
 */
internal val WildHavenLightColors = WildHavenColors(
    sanctuaryPrimary = Color(0xFF2F7D57),
    sanctuarySoft = Color(0xFF7DAA65),
    sanctuaryDeep = Color(0xFF2D5639),
    skyAccent = Color(0xFF376A7A),
    earthAccent = Color(0xFF8A6A32),
    rarityCommon = Color(0xFF8A958C),
    rarityUncommon = Color(0xFF4F8F5B),
    rarityRare = Color(0xFF3D6E9A),
    rarityEpic = Color(0xFF7A4DA8),
    rarityLegendary = Color(0xFFC8A24A),
)

internal val WildHavenDarkColors = WildHavenColors(
    sanctuaryPrimary = Color(0xFF79D0A2),
    sanctuarySoft = Color(0xFF8AB37A),
    sanctuaryDeep = Color(0xFF1F3A28),
    skyAccent = Color(0xFF86C7D7),
    earthAccent = Color(0xFFD5B56D),
    rarityCommon = Color(0xFFA3ADA5),
    rarityUncommon = Color(0xFF7BC089),
    rarityRare = Color(0xFF7CA6D6),
    rarityEpic = Color(0xFFB89DDD),
    rarityLegendary = Color(0xFFE8C97A),
)

internal val LocalWildHavenColors = compositionLocalOf<WildHavenColors> {
    error("WildHavenColors not provided. Wrap your content in WildHavenTheme.")
}

/**
 * 머티리얼 ColorScheme. WildHavenColors와 같은 hex를 공유하므로 두 곳에서
 * 동일한 값을 수정할 때 함께 갱신해야 한다.
 */
internal val WildHavenLightColorScheme = lightColorScheme(
    primary = WildHavenLightColors.sanctuaryPrimary,
    onPrimary = Color.White,
    secondary = WildHavenLightColors.skyAccent,
    tertiary = WildHavenLightColors.earthAccent,
    background = Color(0xFFF7F9F4),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF17201B),
    onSurface = Color(0xFF17201B),
)

internal val WildHavenDarkColorScheme = darkColorScheme(
    primary = WildHavenDarkColors.sanctuaryPrimary,
    onPrimary = Color(0xFF08351F),
    secondary = WildHavenDarkColors.skyAccent,
    tertiary = WildHavenDarkColors.earthAccent,
    background = Color(0xFF101612),
    surface = Color(0xFF19221C),
    onBackground = Color(0xFFE6EFE7),
    onSurface = Color(0xFFE6EFE7),
)

