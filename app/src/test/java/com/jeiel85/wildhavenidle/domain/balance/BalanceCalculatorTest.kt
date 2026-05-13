package com.jeiel85.wildhavenidle.domain.balance

import com.jeiel85.wildhavenidle.data.model.ProtectedAnimal
import com.jeiel85.wildhavenidle.domain.definitions.AnimalDefinitions
import org.junit.Assert.assertEquals
import org.junit.Test

class BalanceCalculatorTest {
    @Test
    fun sanctuaryProductionUsesLevelAsBaseProduction() {
        assertEquals(3.0, BalanceCalculator.calculateSanctuaryProduction(level = 3), 0.0001)
    }

    @Test
    fun totalProductionIncludesAnimalBonusAndArchiveMultiplier() {
        val production = BalanceCalculator.calculateTotalProductionPerSecond(
            sanctuaryLevel = 1,
            protectedAnimals = listOf(ProtectedAnimal(animalId = "rabbit_001", recoveryStage = 1)),
            definitions = AnimalDefinitions.mvpAnimals,
        )

        assertEquals(1.224, production, 0.0001)
    }

    @Test
    fun offlineRewardIsCappedAtEightHoursByDefault() {
        val reward = BalanceCalculator.calculateOfflineReward(
            lastSavedAt = 0L,
            now = 10 * 60 * 60 * 1000L,
            productionPerSecond = 2.0,
        )

        assertEquals(57_600.0, reward, 0.0001)
    }

    @Test
    fun offlineRewardReturnsZeroWhenClockMovesBackwards() {
        val reward = BalanceCalculator.calculateOfflineReward(
            lastSavedAt = 2_000L,
            now = 1_000L,
            productionPerSecond = 2.0,
        )

        assertEquals(0.0, reward, 0.0001)
    }

    @Test
    fun recoveryAndSanctuaryCostsMatchStartingBalance() {
        assertEquals(100L, BalanceCalculator.calculateRecoverySupportCost(baseCost = 100.0, currentRecoveryStage = 1))
        assertEquals(200L, BalanceCalculator.calculateSanctuaryUpgradeCost(currentLevel = 1))
    }
}
