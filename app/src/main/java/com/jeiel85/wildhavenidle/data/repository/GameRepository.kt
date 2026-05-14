package com.jeiel85.wildhavenidle.data.repository

import com.jeiel85.wildhavenidle.core.time.TimeProvider
import com.jeiel85.wildhavenidle.data.local.GameStateDataStore
import com.jeiel85.wildhavenidle.data.model.GameState
import com.jeiel85.wildhavenidle.data.model.ProtectedAnimal
import com.jeiel85.wildhavenidle.domain.balance.BalanceCalculator
import com.jeiel85.wildhavenidle.domain.dailybonus.DailyBonusRules
import com.jeiel85.wildhavenidle.domain.definitions.AnimalDefinitions
import com.jeiel85.wildhavenidle.domain.usecase.CheckUnlockConditionsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GameRepository(
    private val localDataStore: GameStateDataStore,
    private val timeProvider: TimeProvider,
) {
    val gameState: Flow<GameState> = localDataStore.gameState
    private val checkUnlockConditions = CheckUnlockConditionsUseCase()

    suspend fun applyOfflineRewardIfNeeded(): Double {
        val current = gameState.first()
        val now = timeProvider.nowMillis()
        val productionPerSecond = BalanceCalculator.calculateTotalProductionPerSecond(
            sanctuaryLevel = current.sanctuaryLevel,
            protectedAnimals = current.protectedAnimals,
            definitions = AnimalDefinitions.mvpAnimals,
        )
        val offlineReward = BalanceCalculator.calculateOfflineReward(
            lastSavedAt = current.lastSavedAt,
            now = now,
            productionPerSecond = productionPerSecond,
        )

        var updated = current.copy(lastSavedAt = now)
        if (offlineReward > 0.0) {
            updated = updated.copy(carePoint = current.carePoint + offlineReward)
        }
        val final = applyUnlocks(updated, now)
        localDataStore.save(final)

        return offlineReward
    }

    suspend fun addCarePoint(delta: Double) {
        if (delta <= 0.0) return

        val current = gameState.first()
        val now = timeProvider.nowMillis()
        val updated = current.copy(
            carePoint = current.carePoint + delta,
            lastSavedAt = now,
        )
        val final = applyUnlocks(updated, now)
        localDataStore.save(final)
    }

    suspend fun supportAnimalRecovery(animalId: String): Long {
        val current = gameState.first()
        val definition = AnimalDefinitions.mvpAnimals.first { it.id == animalId }
        val animal = current.protectedAnimals.first { it.animalId == animalId }
        val cost = BalanceCalculator.calculateRecoverySupportCost(
            baseCost = BalanceCalculator.getRecoveryBaseCost(definition),
            currentRecoveryStage = animal.recoveryStage,
        )

        if (current.carePoint < cost) return 0L

        val maxStage = definition.maxRecoveryStage
        if (animal.recoveryStage >= maxStage) return 0L

        val updatedAnimals = current.protectedAnimals.map { protected ->
            if (protected.animalId == animalId) {
                protected.copy(recoveryStage = protected.recoveryStage + 1)
            } else {
                protected
            }
        }

        val now = timeProvider.nowMillis()
        val updated = current.copy(
            carePoint = current.carePoint - cost,
            protectedAnimals = updatedAnimals,
            lastSavedAt = now,
        )
        val final = applyUnlocks(updated, now)
        localDataStore.save(final)

        return cost
    }

    suspend fun upgradeSanctuary(): Long {
        val current = gameState.first()
        val cost = BalanceCalculator.calculateSanctuaryUpgradeCost(current.sanctuaryLevel)

        if (current.carePoint < cost) return 0L

        val now = timeProvider.nowMillis()
        val updated = current.copy(
            carePoint = current.carePoint - cost,
            sanctuaryLevel = current.sanctuaryLevel + 1,
            lastSavedAt = now,
        )
        val final = applyUnlocks(updated, now)
        localDataStore.save(final)

        return cost
    }

    suspend fun addProtectedAnimal(animalId: String): Boolean {
        val current = gameState.first()
        val alreadyProtected = current.protectedAnimals.any { it.animalId == animalId }
        if (alreadyProtected) return false

        val now = timeProvider.nowMillis()
        val newAnimal = ProtectedAnimal(
            animalId = animalId,
            recoveryStage = 1,
            protectedAt = now,
        )

        localDataStore.save(
            current.copy(
                protectedAnimals = current.protectedAnimals + newAnimal,
                discoveredAnimalIds = current.discoveredAnimalIds + animalId,
                lastSavedAt = now,
            ),
        )

        return true
    }

    suspend fun resetData() {
        localDataStore.save(GameState.initial(timeProvider.nowMillis()))
    }

    suspend fun markOnboardingCompleted() {
        val current = gameState.first()
        if (current.onboardingCompleted) return

        localDataStore.save(
            current.copy(
                onboardingCompleted = true,
                lastSavedAt = timeProvider.nowMillis(),
            ),
        )
    }

    /**
     * 오늘의 일일 보호 활동 보상을 수령한다. 자격이 없으면 0.0 반환(아무 변화 없음).
     * 보상량은 현재 생산량 기반으로 계산되며 carePoint에 즉시 추가된다.
     */
    suspend fun claimDailyBonus(): Double {
        val current = gameState.first()
        val now = timeProvider.nowMillis()
        if (!DailyBonusRules.isEligible(current.lastDailyBonusClaimedAtMillis, now)) {
            return 0.0
        }

        val productionPerSecond = BalanceCalculator.calculateTotalProductionPerSecond(
            sanctuaryLevel = current.sanctuaryLevel,
            protectedAnimals = current.protectedAnimals,
            definitions = AnimalDefinitions.mvpAnimals,
        )
        val reward = DailyBonusRules.computeReward(productionPerSecond)

        val updated = current.copy(
            carePoint = current.carePoint + reward,
            lastDailyBonusClaimedAtMillis = now,
            lastSavedAt = now,
        )
        val final = applyUnlocks(updated, now)
        localDataStore.save(final)

        return reward
    }

    private fun applyUnlocks(state: GameState, now: Long): GameState {
        val productionPerSecond = BalanceCalculator.calculateTotalProductionPerSecond(
            sanctuaryLevel = state.sanctuaryLevel,
            protectedAnimals = state.protectedAnimals,
            definitions = AnimalDefinitions.mvpAnimals,
        )

        val newlyUnlocked = checkUnlockConditions(
            gameState = state,
            definitions = AnimalDefinitions.mvpAnimals,
            productionPerSecond = productionPerSecond,
        )

        if (newlyUnlocked.isEmpty()) return state

        var updated = state
        for (animalId in newlyUnlocked) {
            updated = updated.copy(
                protectedAnimals = updated.protectedAnimals + ProtectedAnimal(
                    animalId = animalId,
                    recoveryStage = 1,
                    protectedAt = now,
                ),
                discoveredAnimalIds = updated.discoveredAnimalIds + animalId,
            )
        }

        return updated
    }
}
