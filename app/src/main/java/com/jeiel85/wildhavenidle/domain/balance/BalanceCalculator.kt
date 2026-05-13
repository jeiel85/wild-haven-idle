package com.jeiel85.wildhavenidle.domain.balance

import com.jeiel85.wildhavenidle.data.model.AnimalDefinition
import com.jeiel85.wildhavenidle.data.model.ProtectedAnimal
import kotlin.math.pow
import kotlin.math.roundToLong

object BalanceCalculator {
    private const val MAX_OFFLINE_MILLIS = 8 * 60 * 60 * 1000L

    fun calculateSanctuaryProduction(level: Int): Double {
        require(level >= 1) { "Sanctuary level must be at least 1." }
        return level * 1.0
    }

    fun calculateAnimalSupportBonus(
        definition: AnimalDefinition,
        recoveryStage: Int,
    ): Double {
        require(recoveryStage >= 1) { "Recovery stage must be at least 1." }
        return definition.baseSupportBonus * recoveryStage.toDouble().pow(1.15)
    }

    fun calculateArchiveBonusMultiplier(protectedAnimalCount: Int): Double {
        require(protectedAnimalCount >= 0) { "Protected animal count cannot be negative." }
        return 1.0 + protectedAnimalCount * 0.02
    }

    fun calculateTotalProductionPerSecond(
        sanctuaryLevel: Int,
        protectedAnimals: List<ProtectedAnimal>,
        definitions: List<AnimalDefinition>,
    ): Double {
        val definitionsById = definitions.associateBy { it.id }
        val sanctuaryProduction = calculateSanctuaryProduction(sanctuaryLevel)
        val animalBonus = protectedAnimals.sumOf { protected ->
            val definition = definitionsById.getValue(protected.animalId)
            calculateAnimalSupportBonus(definition, protected.recoveryStage)
        }
        val archiveMultiplier = calculateArchiveBonusMultiplier(protectedAnimals.size)

        return (sanctuaryProduction + animalBonus) * archiveMultiplier
    }

    fun calculateOfflineReward(
        lastSavedAt: Long,
        now: Long,
        productionPerSecond: Double,
        maxOfflineMillis: Long = MAX_OFFLINE_MILLIS,
    ): Double {
        require(productionPerSecond >= 0.0) { "Production per second cannot be negative." }
        require(maxOfflineMillis >= 0L) { "Max offline millis cannot be negative." }
        if (now <= lastSavedAt) return 0.0

        val elapsedMillis = now - lastSavedAt
        val rewardedMillis = elapsedMillis.coerceAtMost(maxOfflineMillis)
        val elapsedSeconds = rewardedMillis / 1000.0

        return productionPerSecond * elapsedSeconds
    }

    fun calculateRecoverySupportCost(
        baseCost: Double,
        currentRecoveryStage: Int,
    ): Long {
        require(baseCost >= 0.0) { "Base cost cannot be negative." }
        require(currentRecoveryStage >= 1) { "Recovery stage must be at least 1." }
        return (baseCost * currentRecoveryStage.toDouble().pow(1.45)).roundToLong()
    }

    fun calculateSanctuaryUpgradeCost(currentLevel: Int): Long {
        require(currentLevel >= 1) { "Sanctuary level must be at least 1." }
        return (200.0 * currentLevel.toDouble().pow(1.6)).roundToLong()
    }
}
