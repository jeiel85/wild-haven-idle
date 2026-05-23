package com.jeiel85.wildhavenidle.renew

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Yard
import androidx.compose.ui.graphics.vector.ImageVector
import com.jeiel85.wildhavenidle.data.model.AnimalDefinition
import com.jeiel85.wildhavenidle.data.model.GameState
import com.jeiel85.wildhavenidle.data.model.ProtectedAnimal
import com.jeiel85.wildhavenidle.data.model.UnlockCondition
import com.jeiel85.wildhavenidle.domain.balance.BalanceCalculator
import com.jeiel85.wildhavenidle.domain.definitions.AnimalDefinitions

/**
 * 기존 도메인 모델(`GameState`, `AnimalDefinition`, `ProtectedAnimal`)을 새 디자인 UI가
 * 기대하는 `WildlifeSubject` / `ShelterRestoration` 모양으로 변환한다.
 *
 * 새 UI의 currentRecovery는 0~100 퍼센트, 기존 도메인의 recoveryStage는 1~maxRecoveryStage(20).
 * 둘 사이를 비율로 환산한다.
 *
 * 새 UI의 "discoverCost" 버튼은 도메인의 자동 unlock 모델(`UnlockCondition`)과 맞지 않는다.
 * 일단 표시는 그대로 두되 버튼은 no-op 처리(`WildHavenViewModel.discoverWildlife`).
 */
object DomainMapping {

    /** 5종 동물에 대한 새 UI용 이모지 매핑. 새 디자인의 hero animal 표시에서 사용. */
    fun heroEmojiFor(animalId: String): String = when (animalId) {
        "rabbit_001" -> "🐰"
        "fox_001" -> "🦊"
        "deer_001" -> "🦌"
        "owl_001" -> "🦉"
        "lynx_001" -> "🐈‍⬛"
        else -> "🐾"
    }

    fun cardIconFor(animalId: String): ImageVector = when (animalId) {
        "rabbit_001" -> Icons.Default.Spa
        "fox_001" -> Icons.Default.Eco
        "deer_001" -> Icons.Default.Grass
        "owl_001" -> Icons.Default.Yard
        "lynx_001" -> Icons.Default.Pets
        else -> Icons.Default.Forest
    }

    /** 5종 동물 전체를 새 UI의 WildlifeSubject 리스트로 매핑한다 (보호 중/미발견 모두). */
    fun buildWildlifeList(state: GameState): List<WildlifeSubject> {
        val protectedById = state.protectedAnimals.associateBy { it.animalId }
        return AnimalDefinitions.mvpAnimals.mapIndexed { index, def ->
            toWildlifeSubject(def, protectedById[def.id], observationNumber = index + 1)
        }
    }

    private fun toWildlifeSubject(
        def: AnimalDefinition,
        protectedAnimal: ProtectedAnimal?,
        observationNumber: Int,
    ): WildlifeSubject {
        val isDiscovered = protectedAnimal != null
        val stageValue = protectedAnimal?.recoveryStage ?: 0
        val maxStage = def.maxRecoveryStage
        val percent = if (isDiscovered && maxStage > 0) {
            ((stageValue.toDouble() / maxStage) * 100.0).toInt().coerceIn(0, 100)
        } else {
            0
        }
        val recoveryStage = when {
            !isDiscovered -> RecoveryStage.RESCUED
            stageValue >= maxStage -> RecoveryStage.READY_TO_RETURN
            stageValue <= 1 -> RecoveryStage.RESCUED
            else -> RecoveryStage.REHABILITATING
        }
        val supportCost = if (isDiscovered && stageValue < maxStage) {
            BalanceCalculator.calculateRecoverySupportCost(
                baseCost = BalanceCalculator.getRecoveryBaseCost(def),
                currentRecoveryStage = stageValue.coerceAtLeast(1),
            )
        } else {
            0L
        }
        return WildlifeSubject(
            id = def.id,
            name = def.nameKo,
            englishName = def.species,
            rescueStory = def.descriptionKo,
            currentRecovery = percent,
            observationYield = def.baseSupportBonus,
            currentStage = recoveryStage,
            cardIcon = cardIconFor(def.id),
            discoverCost = 0L,
            isDiscovered = isDiscovered,
            supportCost = supportCost,
            observationNumber = observationNumber,
            unlockHint = if (isDiscovered) "" else unlockHintFor(def.unlockCondition),
        )
    }

    /**
     * 자동 unlock 조건을 사용자가 이해할 수 있는 한국어 안내 텍스트로 변환한다.
     * 새 UI의 잠긴 동물 카드에서 mock의 "비용 + 흔적 발견" 버튼 대신 노출한다.
     */
    private fun unlockHintFor(condition: UnlockCondition): String = when (condition) {
        UnlockCondition.InitialAnimal -> "보호구역의 첫 구조 동물입니다."
        is UnlockCondition.CarePointReached ->
            "보호 포인트 ${condition.amount}p 누적 시 보호구역에 합류"
        is UnlockCondition.RecoveryStageReached ->
            "${animalNameFor(condition.animalId)} 회복 ${condition.stage}단계 도달 시 합류"
        is UnlockCondition.TotalProductionReached ->
            "초당 생산량 +%.1f 도달 시 합류".format(condition.productionPerSecond)
        is UnlockCondition.ProtectedAnimalCountReached ->
            "보호 동물 ${condition.count}마리 누적 시 합류"
    }

    private fun animalNameFor(id: String): String =
        AnimalDefinitions.mvpAnimals.firstOrNull { it.id == id }?.nameKo ?: id

    /**
     * 새 UI는 4개의 환경 복원 카드를 보여준다. v0.6.1에서는 첫 번째만 실제 sanctuary 업그레이드와
     * 연결하고, 나머지 3개는 비활성("다음 패치 예고") 카드로 표시한다.
     *
     * 첫 번째 카드의 `currentLevel`은 실제 sanctuaryLevel을 반영해서 사용자가 직전 진행 상태를
     * 확인할 수 있게 한다.
     */
    fun buildRestorations(state: GameState): List<ShelterRestoration> {
        val sanctuaryUpgradeCost = BalanceCalculator.calculateSanctuaryUpgradeCost(state.sanctuaryLevel)
        return listOf(
            ShelterRestoration(
                id = SANCTUARY_MAIN_ID,
                name = "보호구역 환경 정비",
                currentLevel = state.sanctuaryLevel,
                baseCost = sanctuaryUpgradeCost,
                costMultiplier = 1.0, // 다음 비용은 도메인이 계산하므로 multiplier 미사용
                rateAddition = 1.0,
                description = "보호구역 전반의 환경을 단계적으로 회복합니다. 레벨이 오르면 보호구역의 기본 회복 효율이 함께 증가합니다.",
                icon = Icons.Default.WaterDrop,
            ),
            ShelterRestoration(
                id = "forest_path_planned",
                name = "조용한 숲길 정비",
                currentLevel = 0,
                baseCost = 0L,
                rateAddition = 0.0,
                description = "다음 업데이트에서 추가됩니다.",
                icon = Icons.Default.Forest,
            ),
            ShelterRestoration(
                id = "shrub_planned",
                name = "밀집 관목 덤불숲 확장",
                currentLevel = 0,
                baseCost = 0L,
                rateAddition = 0.0,
                description = "다음 업데이트에서 추가됩니다.",
                icon = Icons.Default.NaturePeople,
            ),
            ShelterRestoration(
                id = "care_center_planned",
                name = "종합 자연 치유 지원소",
                currentLevel = 0,
                baseCost = 0L,
                rateAddition = 0.0,
                description = "다음 업데이트에서 추가됩니다.",
                icon = Icons.Default.MedicalServices,
            ),
        )
    }

    const val SANCTUARY_MAIN_ID = "sanctuary_main"

    fun isRestorationEnabled(id: String): Boolean = id == SANCTUARY_MAIN_ID
}
