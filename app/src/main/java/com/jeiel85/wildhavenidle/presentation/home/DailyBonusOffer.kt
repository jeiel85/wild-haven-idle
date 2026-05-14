package com.jeiel85.wildhavenidle.presentation.home

/**
 * 오늘 자격이 있을 때만 노출되는 일일 보호 활동 보상 카드의 표현 모델.
 * null이면 카드 자체가 화면에 노출되지 않는다.
 */
data class DailyBonusOffer(
    val rewardAmount: Double,
)
