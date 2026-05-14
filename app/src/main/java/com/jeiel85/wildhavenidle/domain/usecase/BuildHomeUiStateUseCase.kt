package com.jeiel85.wildhavenidle.domain.usecase

import com.jeiel85.wildhavenidle.core.format.NumberFormatter
import com.jeiel85.wildhavenidle.data.model.AnimalDefinition
import com.jeiel85.wildhavenidle.data.model.GameState
import com.jeiel85.wildhavenidle.data.model.UnlockCondition
import com.jeiel85.wildhavenidle.domain.balance.BalanceCalculator
import com.jeiel85.wildhavenidle.domain.definitions.AnimalDefinitions
import com.jeiel85.wildhavenidle.presentation.home.AnimalRecoveryItem
import com.jeiel85.wildhavenidle.presentation.home.HomeUiState
import com.jeiel85.wildhavenidle.presentation.home.NextUnlockProgress
import kotlin.math.max

class BuildHomeUiStateUseCase {
    private val supportUseCase = SupportAnimalRecoveryUseCase()
    private val upgradeUseCase = UpgradeSanctuaryUseCase()

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

        val recoveryItems = animals.mapNotNull { def ->
            val protected = gameState.protectedAnimals.firstOrNull { it.animalId == def.id }
                ?: return@mapNotNull null
            AnimalRecoveryItem(
                definition = def,
                protectedAnimal = protected,
                recoveryCost = supportUseCase.getCost(def, protected.recoveryStage),
                supportBonus = BalanceCalculator.calculateAnimalSupportBonus(def, protected.recoveryStage),
            )
        }

        val tapReward = max(1.0, productionPerSecond)

        return HomeUiState(
            carePoint = gameState.carePoint,
            sanctuaryLevel = gameState.sanctuaryLevel,
            productionPerSecond = productionPerSecond,
            protectedAnimalCount = gameState.protectedAnimals.size,
            discoveredAnimalCount = gameState.discoveredAnimalIds.size,
            totalAnimalCount = animals.size,
            offlineReward = offlineReward,
            sanctuaryUpgradeCost = upgradeUseCase.getCost(gameState.sanctuaryLevel),
            tapReward = tapReward,
            animals = recoveryItems,
            protectedAnimalIds = gameState.protectedAnimals.map { it.animalId },
            nextUnlock = buildNextUnlock(gameState, productionPerSecond, animals),
        )
    }

    private fun buildNextUnlock(
        state: GameState,
        productionPerSecond: Double,
        definitions: List<AnimalDefinition>,
    ): NextUnlockProgress? {
        val nextDef = definitions.firstOrNull { it.id !in state.discoveredAnimalIds } ?: return null
        val (progress, helper) = when (val cond = nextDef.unlockCondition) {
            UnlockCondition.InitialAnimal -> 1f to "준비 완료"
            is UnlockCondition.CarePointReached -> {
                val p = (state.carePoint / cond.amount).coerceIn(0.0, 1.0).toFloat()
                val helper = "${NumberFormatter.compact(state.carePoint)} / ${NumberFormatter.compact(cond.amount.toDouble())} 포인트"
                p to helper
            }
            is UnlockCondition.RecoveryStageReached -> {
                val cur = state.protectedAnimals.firstOrNull { it.animalId == cond.animalId }?.recoveryStage ?: 0
                val p = (cur.toDouble() / cond.stage).coerceIn(0.0, 1.0).toFloat()
                val targetName = definitions.firstOrNull { it.id == cond.animalId }?.nameKo ?: cond.animalId
                p to "$targetName 회복 $cur / ${cond.stage}"
            }
            is UnlockCondition.TotalProductionReached -> {
                val p = (productionPerSecond / cond.productionPerSecond).coerceIn(0.0, 1.0).toFloat()
                val helper = "${NumberFormatter.perSecond(productionPerSecond)} / ${NumberFormatter.perSecond(cond.productionPerSecond)}"
                p to helper
            }
            is UnlockCondition.ProtectedAnimalCountReached -> {
                val cur = state.protectedAnimals.size
                val p = (cur.toDouble() / cond.count).coerceIn(0.0, 1.0).toFloat()
                p to "보호 종 $cur / ${cond.count}"
            }
        }
        return NextUnlockProgress(
            animalNameKo = nextDef.nameKo,
            progress = progress,
            helperText = helper,
        )
    }
}
