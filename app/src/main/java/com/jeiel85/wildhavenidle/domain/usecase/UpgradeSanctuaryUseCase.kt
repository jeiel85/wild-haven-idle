package com.jeiel85.wildhavenidle.domain.usecase

import com.jeiel85.wildhavenidle.domain.balance.BalanceCalculator

class UpgradeSanctuaryUseCase {
    fun getCost(currentLevel: Int): Long =
        BalanceCalculator.calculateSanctuaryUpgradeCost(currentLevel)

    fun canUpgrade(currentCarePoint: Double, cost: Long): Boolean =
        currentCarePoint >= cost && cost > 0L
}
