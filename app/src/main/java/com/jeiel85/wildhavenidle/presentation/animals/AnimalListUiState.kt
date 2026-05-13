package com.jeiel85.wildhavenidle.presentation.animals

import com.jeiel85.wildhavenidle.data.model.AnimalDefinition
import com.jeiel85.wildhavenidle.data.model.ProtectedAnimal

data class AnimalListItem(
    val definition: AnimalDefinition,
    val protectedAnimal: ProtectedAnimal?,
    val recoveryCost: Long,
    val supportBonus: Double,
)

data class AnimalListUiState(
    val carePoint: Double = 0.0,
    val sanctuaryLevel: Int = 1,
    val sanctuaryUpgradeCost: Long = 0L,
    val productionPerSecond: Double = 0.0,
    val animals: List<AnimalListItem> = emptyList(),
)
