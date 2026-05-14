package com.jeiel85.wildhavenidle.presentation.home

/**
 * 홈 화면 상단 "지금 추천" 카드에 노출되는 단일 행동 추천.
 *
 * 우선순위 (단순·예측 가능하게 설계):
 * 1. [UpgradeSanctuary] — 보호구역 확장이 가능하면 우선 (기본 생산량 +1/sec, 가장 안정적인 ROI)
 * 2. [SupportRecovery] — 그렇지 않고 회복 지원 가능한 동물이 있으면, 생산량 증가량 / 비용 비율이
 *    가장 좋은 동물을 추천
 * 3. [WaitForNext] — 둘 다 불가하면 다음 가능 행동까지 대기 시간 안내
 *
 * 이 추천은 *기존 카드를 대체하지 않는다.* 하단에는 모든 카드가 그대로 보이고, 추천 카드는
 * 단순히 사용자에게 "지금 무엇을 하면 좋은지" 한 가지를 가볍게 안내하는 역할이다.
 */
sealed interface RecommendedAction {

    /** 사용자가 즉시 실행할 수 있는 행동을 의미하는 마커. */
    sealed interface Actionable : RecommendedAction {
        val titleKo: String
        val costLabel: String
        val effectLabel: String
        val ctaLabelKo: String
    }

    data class UpgradeSanctuary(
        val nextLevel: Int,
        override val costLabel: String,
        override val effectLabel: String = "생산량 +1/sec",
    ) : Actionable {
        override val titleKo: String = "보호구역 확장 — Lv.$nextLevel"
        override val ctaLabelKo: String = "확장하기"
    }

    data class SupportRecovery(
        val animalId: String,
        val animalNameKo: String,
        val nextStage: Int,
        override val costLabel: String,
        override val effectLabel: String,
    ) : Actionable {
        override val titleKo: String = "$animalNameKo 회복 — Lv.$nextStage"
        override val ctaLabelKo: String = "회복 지원"
    }

    /** 가능한 행동이 없을 때. 가장 가까운 다음 행동까지 남은 초/필요 포인트를 안내한다. */
    data class WaitForNext(
        val secondsUntilNext: Long,
        val targetLabelKo: String,
    ) : RecommendedAction
}
