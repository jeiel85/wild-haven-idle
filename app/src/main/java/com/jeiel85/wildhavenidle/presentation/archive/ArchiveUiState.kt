package com.jeiel85.wildhavenidle.presentation.archive

import com.jeiel85.wildhavenidle.data.model.ArchiveState
import com.jeiel85.wildhavenidle.data.model.AnimalDefinition
import com.jeiel85.wildhavenidle.data.model.ProtectedAnimal

data class ArchiveItem(
    val definition: AnimalDefinition,
    val state: ArchiveState,
    val protectedAnimal: ProtectedAnimal?,
    val supportBonus: Double,
)

data class ArchiveUiState(
    val items: List<ArchiveItem> = emptyList(),
    val totalCount: Int = 0,
    val discoveredCount: Int = 0,
    val protectedCount: Int = 0,
)
