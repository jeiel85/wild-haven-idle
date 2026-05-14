package com.jeiel85.wildhavenidle.domain.usecase

import com.jeiel85.wildhavenidle.core.format.NumberFormatter
import com.jeiel85.wildhavenidle.data.model.AnimalDefinition
import com.jeiel85.wildhavenidle.data.model.GameState
import com.jeiel85.wildhavenidle.data.model.UnlockCondition
import com.jeiel85.wildhavenidle.domain.balance.BalanceCalculator
import com.jeiel85.wildhavenidle.domain.definitions.AnimalDefinitions
import com.jeiel85.wildhavenidle.domain.dailybonus.DailyBonusRules
import com.jeiel85.wildhavenidle.presentation.home.AnimalRecoveryItem
import com.jeiel85.wildhavenidle.presentation.home.DailyBonusOffer
import com.jeiel85.wildhavenidle.presentation.home.HomeUiState
import com.jeiel85.wildhavenidle.presentation.home.NextUnlockProgress
import com.jeiel85.wildhavenidle.presentation.home.RecommendedAction
import kotlin.math.ceil
import kotlin.math.max

class BuildHomeUiStateUseCase {
    private val supportUseCase = SupportAnimalRecoveryUseCase()
    private val upgradeUseCase = UpgradeSanctuaryUseCase()

    operator fun invoke(
        gameState: GameState,
        offlineReward: Double?,
        nowMillis: Long = System.currentTimeMillis(),
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
        val sanctuaryUpgradeCost = upgradeUseCase.getCost(gameState.sanctuaryLevel)

        return HomeUiState(
            carePoint = gameState.carePoint,
            sanctuaryLevel = gameState.sanctuaryLevel,
            productionPerSecond = productionPerSecond,
            protectedAnimalCount = gameState.protectedAnimals.size,
            discoveredAnimalCount = gameState.discoveredAnimalIds.size,
            totalAnimalCount = animals.size,
            offlineReward = offlineReward,
            sanctuaryUpgradeCost = sanctuaryUpgradeCost,
            tapReward = tapReward,
            animals = recoveryItems,
            protectedAnimalIds = gameState.protectedAnimals.map { it.animalId },
            nextUnlock = buildNextUnlock(gameState, productionPerSecond, animals),
            showOnboarding = !gameState.onboardingCompleted,
            recommendedAction = recommendAction(
                carePoint = gameState.carePoint,
                sanctuaryLevel = gameState.sanctuaryLevel,
                sanctuaryUpgradeCost = sanctuaryUpgradeCost,
                recoveryItems = recoveryItems,
                productionPerSecond = productionPerSecond,
            ),
            dailyBonus = if (DailyBonusRules.isEligible(gameState.lastDailyBonusClaimedAtMillis, nowMillis)) {
                DailyBonusOffer(rewardAmount = DailyBonusRules.computeReward(productionPerSecond))
            } else {
                null
            },
        )
    }

    /**
     * 우선순위:
     * 1. 보호구역 확장 가능 → UpgradeSanctuary
     * 2. 회복 가능한 동물 중 supportBonus / recoveryCost 비율 최고 → SupportRecovery
     * 3. 둘 다 불가 → 가장 싼 다음 행동까지 대기 안내 (WaitForNext)
     *
     * 비용 0(이미 끝난 동작) 항목은 자동 제외.
     */
    private fun recommendAction(
        carePoint: Double,
        sanctuaryLevel: Int,
        sanctuaryUpgradeCost: Long,
        recoveryItems: List<AnimalRecoveryItem>,
        productionPerSecond: Double,
    ): RecommendedAction {
        val canUpgrade = sanctuaryUpgradeCost > 0L && carePoint >= sanctuaryUpgradeCost
        if (canUpgrade) {
            return RecommendedAction.UpgradeSanctuary(
                nextLevel = sanctuaryLevel + 1,
                costLabel = "${NumberFormatter.compact(sanctuaryUpgradeCost.toDouble())} 포인트",
            )
        }

        val recoverableNow = recoveryItems
            .filter { item ->
                val notMaxed = item.protectedAnimal.recoveryStage < item.definition.maxRecoveryStage
                val affordable = item.recoveryCost > 0L && carePoint >= item.recoveryCost
                notMaxed && affordable
            }
            .maxByOrNull { item ->
                // 비용 대비 생산량 증가량이 가장 큰 동물 (단순한 ROI 척도).
                item.supportBonus / item.recoveryCost.toDouble()
            }
        if (recoverableNow != null) {
            return RecommendedAction.SupportRecovery(
                animalId = recoverableNow.definition.id,
                animalNameKo = recoverableNow.definition.nameKo,
                nextStage = recoverableNow.protectedAnimal.recoveryStage + 1,
                costLabel = "${NumberFormatter.compact(recoverableNow.recoveryCost.toDouble())} 포인트",
                effectLabel = "생산량 +${NumberFormatter.perSecond(recoverableNow.supportBonus)}",
            )
        }

        return waitForNext(
            carePoint = carePoint,
            sanctuaryUpgradeCost = sanctuaryUpgradeCost,
            recoveryItems = recoveryItems,
            productionPerSecond = productionPerSecond,
        )
    }

    private fun waitForNext(
        carePoint: Double,
        sanctuaryUpgradeCost: Long,
        recoveryItems: List<AnimalRecoveryItem>,
        productionPerSecond: Double,
    ): RecommendedAction.WaitForNext {
        // 가능한 다음 행동 후보(아직 못 산 것들) 중 가장 적게 모자란 것을 고른다.
        val candidates = mutableListOf<Pair<Long, String>>()
        if (sanctuaryUpgradeCost > 0L) {
            candidates += sanctuaryUpgradeCost to "보호구역 확장"
        }
        recoveryItems.forEach { item ->
            if (item.recoveryCost > 0L &&
                item.protectedAnimal.recoveryStage < item.definition.maxRecoveryStage
            ) {
                candidates += item.recoveryCost to "${item.definition.nameKo} 회복"
            }
        }

        val cheapest = candidates.minByOrNull { it.first }
            ?: return RecommendedAction.WaitForNext(
                secondsUntilNext = 0L,
                targetLabelKo = "다음 보호 행동",
            )

        val deficit = (cheapest.first - carePoint).coerceAtLeast(0.0)
        val seconds = if (productionPerSecond > 0.0) {
            ceil(deficit / productionPerSecond).toLong()
        } else {
            Long.MAX_VALUE
        }
        return RecommendedAction.WaitForNext(
            secondsUntilNext = seconds,
            targetLabelKo = cheapest.second,
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
