package com.jeiel85.wildhavenidle.domain.usecase

import com.jeiel85.wildhavenidle.data.model.GameState
import com.jeiel85.wildhavenidle.domain.balance.BalanceCalculator
import com.jeiel85.wildhavenidle.domain.definitions.AnimalDefinitions
import com.jeiel85.wildhavenidle.presentation.home.HomeUiState

class BuildHomeUiStateUseCase {
    operator fun invoke(
        gameState: GameState,
        offlineReward: Double?,
    ): HomeUiState {
        val animals = AnimalDefinitions.mvpAnimals
        val productionPerSecond = BalanceCalculator.calculateTotalProductionPerSecond(
            sanctuaryLevel = gameState.sanctuaryLevel,
            protectedAnimals = gameState.protectedAnimals,
            definitions = animals,
        )

        return HomeUiState(
            carePoint = gameState.carePoint,
            sanctuaryLevel = gameState.sanctuaryLevel,
            productionPerSecond = productionPerSecond,
            protectedAnimalCount = gameState.protectedAnimals.size,
            discoveredAnimalCount = gameState.discoveredAnimalIds.size,
            totalAnimalCount = animals.size,
            offlineReward = offlineReward,
        )
    }
}
