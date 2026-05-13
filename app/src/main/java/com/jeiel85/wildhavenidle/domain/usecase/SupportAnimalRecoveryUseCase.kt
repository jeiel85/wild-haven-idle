package com.jeiel85.wildhavenidle.domain.usecase

import com.jeiel85.wildhavenidle.data.model.AnimalDefinition
import com.jeiel85.wildhavenidle.domain.balance.BalanceCalculator

class SupportAnimalRecoveryUseCase {
    fun getCost(definition: AnimalDefinition, currentStage: Int): Long =
        BalanceCalculator.calculateRecoverySupportCost(
            baseCost = BalanceCalculator.getRecoveryBaseCost(definition),
            currentRecoveryStage = currentStage,
        )

    fun canSupport(currentCarePoint: Double, cost: Long): Boolean =
        currentCarePoint >= cost && cost > 0L
}
