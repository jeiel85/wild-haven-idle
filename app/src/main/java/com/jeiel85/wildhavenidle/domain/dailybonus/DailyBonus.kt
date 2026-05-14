package com.jeiel85.wildhavenidle.domain.dailybonus

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.max

/**
 * 일일 보호 활동 보상.
 *
 * - 광고/결제와 무관한 *순수 로컬 보상*. 외부 SDK 없음.
 * - "1일 1회"는 *시각이 아니라 로컬 자정 경계로 나뉜 달력 날짜*로 판정한다.
 *   같은 24시간이라도 자정을 넘기면 다시 받을 수 있고, 자정을 넘기지 않았다면
 *   며칠 후에 다시 들어와도 받을 수 없다.
 * - 보상량은 *현재 생산량 × 3시간*. 후반 진행도에 자연스럽게 스케일된다.
 *   매우 초반(생산량 거의 0)에서도 의미 있도록 [MIN_BONUS_POINTS] 하한.
 *
 * 윤리 메모: 접속하지 않으면 동물이 학대받는 식의 표현을 피한다. 카드 문구는
 * "오늘의 보호 활동 보상"으로 사용자 자율성을 존중한다 (`AGENTS.md` §9).
 */
object DailyBonusRules {

    /** 보상 산출에 쓰는 "하루치 가치"의 의미. 오프라인 보상(8시간 상한)보다 작게 잡아 일관성 유지. */
    const val REWARD_PRODUCTION_HOURS = 3.0

    /** 매우 초반에도 누르는 의미가 있도록 하한. */
    const val MIN_BONUS_POINTS = 50.0

    /** 한 번도 받지 않았으면 항상 자격 있음. 그 외에는 마지막 수령 날짜가 오늘 이전이어야 자격. */
    fun isEligible(
        lastClaimedAtMillis: Long?,
        nowMillis: Long,
        zone: ZoneId = ZoneId.systemDefault(),
    ): Boolean {
        if (lastClaimedAtMillis == null) return true
        val lastDate = toLocalDate(lastClaimedAtMillis, zone)
        val today = toLocalDate(nowMillis, zone)
        return today.isAfter(lastDate)
    }

    /** 보상량(보호 포인트). 자격 여부와 무관한 순수 계산. */
    fun computeReward(productionPerSecond: Double): Double {
        val productionBased = productionPerSecond * REWARD_PRODUCTION_HOURS * 3600.0
        return max(MIN_BONUS_POINTS, productionBased)
    }

    private fun toLocalDate(millis: Long, zone: ZoneId): LocalDate =
        Instant.ofEpochMilli(millis).atZone(zone).toLocalDate()
}
