package com.jeiel85.wildhavenidle.presentation.home

import com.jeiel85.wildhavenidle.data.model.AnimalDefinition
import com.jeiel85.wildhavenidle.data.model.ProtectedAnimal

data class HomeUiState(
    val carePoint: Double = 0.0,
    val sanctuaryLevel: Int = 1,
    val productionPerSecond: Double = 0.0,
    val protectedAnimalCount: Int = 0,
    val discoveredAnimalCount: Int = 0,
    val totalAnimalCount: Int = 0,
    val offlineReward: Double? = null,
    val sanctuaryUpgradeCost: Long = 0L,
    val tapReward: Double = 1.0,
    val animals: List<AnimalRecoveryItem> = emptyList(),
    val protectedAnimalIds: List<String> = emptyList(),
    val nextUnlock: NextUnlockProgress? = null,
)

data class AnimalRecoveryItem(
    val definition: AnimalDefinition,
    val protectedAnimal: ProtectedAnimal,
    val recoveryCost: Long,
    val supportBonus: Double,
)

data class NextUnlockProgress(
    val animalNameKo: String,
    val progress: Float,
    val helperText: String,
)
