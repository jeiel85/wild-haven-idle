package com.jeiel85.wildhavenidle.data.repository

import com.jeiel85.wildhavenidle.core.time.TimeProvider
import com.jeiel85.wildhavenidle.data.local.GameStateDataStore
import com.jeiel85.wildhavenidle.data.model.GameState
import com.jeiel85.wildhavenidle.domain.balance.BalanceCalculator
import com.jeiel85.wildhavenidle.domain.definitions.AnimalDefinitions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GameRepository(
    private val localDataStore: GameStateDataStore,
    private val timeProvider: TimeProvider,
) {
    val gameState: Flow<GameState> = localDataStore.gameState

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

        if (offlineReward > 0.0) {
            localDataStore.save(
                current.copy(
                    carePoint = current.carePoint + offlineReward,
                    lastSavedAt = now,
                ),
            )
        } else {
            localDataStore.save(current.copy(lastSavedAt = now))
        }

        return offlineReward
    }

    suspend fun addCarePoint(delta: Double) {
        if (delta <= 0.0) return

        val current = gameState.first()
        localDataStore.save(
            current.copy(
                carePoint = current.carePoint + delta,
                lastSavedAt = timeProvider.nowMillis(),
            ),
        )
    }
}
