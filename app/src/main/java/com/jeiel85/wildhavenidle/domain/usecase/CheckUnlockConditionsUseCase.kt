package com.jeiel85.wildhavenidle.domain.usecase

import com.jeiel85.wildhavenidle.data.model.AnimalDefinition
import com.jeiel85.wildhavenidle.data.model.GameState
import com.jeiel85.wildhavenidle.data.model.UnlockCondition

class CheckUnlockConditionsUseCase {
    operator fun invoke(
        gameState: GameState,
        definitions: List<AnimalDefinition>,
        productionPerSecond: Double,
    ): List<String> {
        val protectedIds = gameState.protectedAnimals.map { it.animalId }.toSet()

        return definitions.filter { def ->
            def.id !in protectedIds && when (val condition = def.unlockCondition) {
                is UnlockCondition.InitialAnimal -> false
                is UnlockCondition.CarePointReached -> gameState.carePoint >= condition.amount
                is UnlockCondition.RecoveryStageReached -> {
                    gameState.protectedAnimals.any {
                        it.animalId == condition.animalId && it.recoveryStage >= condition.stage
                    }
                }
                is UnlockCondition.TotalProductionReached -> productionPerSecond >= condition.productionPerSecond
                is UnlockCondition.ProtectedAnimalCountReached -> gameState.protectedAnimals.size >= condition.count
            }
        }.map { it.id }
    }
}
