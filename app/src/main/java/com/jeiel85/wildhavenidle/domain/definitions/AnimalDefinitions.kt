package com.jeiel85.wildhavenidle.domain.definitions

import com.jeiel85.wildhavenidle.data.model.AnimalDefinition
import com.jeiel85.wildhavenidle.data.model.HabitatType
import com.jeiel85.wildhavenidle.data.model.ProtectedAnimal
import com.jeiel85.wildhavenidle.data.model.Rarity
import com.jeiel85.wildhavenidle.data.model.UnlockCondition

object AnimalDefinitions {
    val mvpAnimals = listOf(
        AnimalDefinition(
            id = "rabbit_001",
            nameKo = "숲토끼",
            nameEn = "Forest Rabbit",
            species = "Leporidae",
            rarity = Rarity.COMMON,
            baseSupportBonus = 0.2,
            maxRecoveryStage = 20,
            habitatType = HabitatType.FOREST,
            descriptionKo = "숲 가장자리에서 구조되어 조용한 환경에 적응 중입니다.",
            descriptionEn = "Rescued near a forest edge and adapting to a quiet habitat.",
            unlockCondition = UnlockCondition.InitialAnimal,
        ),
        AnimalDefinition(
            id = "fox_001",
            nameKo = "붉은여우",
            nameEn = "Red Fox",
            species = "Vulpes vulpes",
            rarity = Rarity.COMMON,
            baseSupportBonus = 0.5,
            maxRecoveryStage = 20,
            habitatType = HabitatType.FOREST,
            descriptionKo = "회복 공간과 은신처가 충분할 때 보호구역에 합류합니다.",
            descriptionEn = "Joins the sanctuary when recovery space and cover are available.",
            unlockCondition = UnlockCondition.CarePointReached(300),
        ),
        AnimalDefinition(
            id = "deer_001",
            nameKo = "어린 사슴",
            nameEn = "Young Deer",
            species = "Cervidae",
            rarity = Rarity.UNCOMMON,
            baseSupportBonus = 1.2,
            maxRecoveryStage = 20,
            habitatType = HabitatType.FOREST,
            descriptionKo = "초기 회복을 마친 동물이 있어야 안정적으로 보호할 수 있습니다.",
            descriptionEn = "Can be protected after another animal reaches stable recovery.",
            unlockCondition = UnlockCondition.RecoveryStageReached("rabbit_001", 5),
        ),
        AnimalDefinition(
            id = "owl_001",
            nameKo = "밤부엉이",
            nameEn = "Night Owl",
            species = "Strigiformes",
            rarity = Rarity.UNCOMMON,
            baseSupportBonus = 1.8,
            maxRecoveryStage = 20,
            habitatType = HabitatType.FOREST,
            descriptionKo = "보호구역 운영이 안정되면 야간 보호 공간에 머뭅니다.",
            descriptionEn = "Stays in night shelters once the sanctuary is running steadily.",
            unlockCondition = UnlockCondition.TotalProductionReached(5.0),
        ),
        AnimalDefinition(
            id = "lynx_001",
            nameKo = "스라소니",
            nameEn = "Lynx",
            species = "Lynx lynx",
            rarity = Rarity.RARE,
            baseSupportBonus = 3.5,
            maxRecoveryStage = 20,
            habitatType = HabitatType.MOUNTAIN,
            descriptionKo = "여러 구조 동물을 안정적으로 보호한 뒤 합류하는 희귀 동물입니다.",
            descriptionEn = "A rare animal that joins after several rescues are protected.",
            unlockCondition = UnlockCondition.ProtectedAnimalCountReached(4),
        ),
    )

    fun initialProtectedAnimals(now: Long = System.currentTimeMillis()): List<ProtectedAnimal> =
        listOf(
            ProtectedAnimal(
                animalId = "rabbit_001",
                recoveryStage = 1,
                protectedAt = now,
            ),
        )
}
