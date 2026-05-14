package com.jeiel85.wildhavenidle.domain.dailybonus

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

class DailyBonusRulesTest {

    private val seoul: ZoneId = ZoneId.of("Asia/Seoul")

    /** 특정 날짜의 자정(00:00) UTC 밀리스. 테스트 가독성용. */
    private fun millisAt(date: LocalDate, zone: ZoneId): Long =
        date.atStartOfDay(zone).toInstant().toEpochMilli()

    @Test
    fun isEligibleWhenNeverClaimed() {
        val now = millisAt(LocalDate.of(2026, 5, 14), seoul)
        assertTrue(DailyBonusRules.isEligible(lastClaimedAtMillis = null, nowMillis = now, zone = seoul))
    }

    @Test
    fun isNotEligibleSameCalendarDay() {
        val today = LocalDate.of(2026, 5, 14)
        val noon = today.atTime(12, 0).atZone(seoul).toInstant().toEpochMilli()
        val evening = today.atTime(23, 50).atZone(seoul).toInstant().toEpochMilli()

        assertFalse(DailyBonusRules.isEligible(lastClaimedAtMillis = noon, nowMillis = evening, zone = seoul))
    }

    @Test
    fun isEligibleAfterMidnightCrossing() {
        val late = LocalDate.of(2026, 5, 14).atTime(23, 59).atZone(seoul).toInstant().toEpochMilli()
        val early = LocalDate.of(2026, 5, 15).atTime(0, 1).atZone(seoul).toInstant().toEpochMilli()

        assertTrue(DailyBonusRules.isEligible(lastClaimedAtMillis = late, nowMillis = early, zone = seoul))
    }

    @Test
    fun timeZoneAffectsBoundary() {
        // 서울에서는 자정을 넘었지만, UTC에서는 같은 날.
        val seoulMidnightCross = LocalDate.of(2026, 5, 15).atTime(0, 30).atZone(seoul).toInstant().toEpochMilli()
        val previousNoonSeoul = LocalDate.of(2026, 5, 14).atTime(12, 0).atZone(seoul).toInstant().toEpochMilli()

        assertTrue(DailyBonusRules.isEligible(previousNoonSeoul, seoulMidnightCross, seoul))
        assertFalse(
            "UTC 기준으로는 아직 같은 날 (15시 30분) — 자격 없음",
            DailyBonusRules.isEligible(previousNoonSeoul, seoulMidnightCross, ZoneOffset.UTC),
        )
    }

    @Test
    fun rewardScalesWithProductionRate() {
        // 0.001/sec × 10800초 = 10.8 → 하한 50으로 클램프
        val veryLow = DailyBonusRules.computeReward(productionPerSecond = 0.001)
        // 0.1/sec × 10800초 = 1080 (하한 초과 — 생산량 기반 적용)
        val low = DailyBonusRules.computeReward(productionPerSecond = 0.1)
        // 5/sec × 10800초 = 54000
        val mid = DailyBonusRules.computeReward(productionPerSecond = 5.0)
        val high = DailyBonusRules.computeReward(productionPerSecond = 100.0)

        assertEquals("매우 저생산은 하한 적용", DailyBonusRules.MIN_BONUS_POINTS, veryLow, 0.0001)
        assertEquals(0.1 * 3 * 3600.0, low, 0.0001)
        assertEquals(5.0 * 3 * 3600.0, mid, 0.0001)
        assertEquals(100.0 * 3 * 3600.0, high, 0.0001)
        assertTrue("생산량이 클수록 보상도 큼", low < mid && mid < high)
    }

    @Test
    fun rewardClampsToMinimumWhenProductionIsZero() {
        val r = DailyBonusRules.computeReward(productionPerSecond = 0.0)
        assertEquals(DailyBonusRules.MIN_BONUS_POINTS, r, 0.0001)
    }
}
