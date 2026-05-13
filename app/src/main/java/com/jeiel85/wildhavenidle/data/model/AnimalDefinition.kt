package com.jeiel85.wildhavenidle.data.model

data class AnimalDefinition(
    val id: String,
    val nameKo: String,
    val nameEn: String,
    val species: String,
    val rarity: Rarity,
    val baseSupportBonus: Double,
    val maxRecoveryStage: Int,
    val habitatType: HabitatType,
    val descriptionKo: String,
    val descriptionEn: String,
    val unlockCondition: UnlockCondition,
)

enum class Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
}

enum class HabitatType {
    FOREST,
    WETLAND,
    GRASSLAND,
    MOUNTAIN,
    COAST,
}

sealed class UnlockCondition {
    data object InitialAnimal : UnlockCondition()
    data class CarePointReached(val amount: Long) : UnlockCondition()
    data class RecoveryStageReached(val animalId: String, val stage: Int) : UnlockCondition()
    data class TotalProductionReached(val productionPerSecond: Double) : UnlockCondition()
    data class ProtectedAnimalCountReached(val count: Int) : UnlockCondition()
}
