package com.jeiel85.wildhavenidle.presentation.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeiel85.wildhavenidle.data.model.ArchiveState
import com.jeiel85.wildhavenidle.data.repository.GameRepository
import com.jeiel85.wildhavenidle.domain.balance.BalanceCalculator
import com.jeiel85.wildhavenidle.domain.definitions.AnimalDefinitions
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ArchiveViewModel(
    gameRepository: GameRepository,
) : ViewModel() {
    val uiState = gameRepository.gameState
        .map { gameState ->
            val definitions = AnimalDefinitions.mvpAnimals
            val protectedMap = gameState.protectedAnimals.associateBy { it.animalId }

            val items = definitions.map { def ->
                val protected = protectedMap[def.id]
                val state = when {
                    protected != null -> ArchiveState.PROTECTED
                    gameState.discoveredAnimalIds.contains(def.id) -> ArchiveState.DISCOVERED
                    else -> ArchiveState.LOCKED
                }
                val bonus = protected?.let {
                    BalanceCalculator.calculateAnimalSupportBonus(def, it.recoveryStage)
                } ?: def.baseSupportBonus

                ArchiveItem(
                    definition = def,
                    state = state,
                    protectedAnimal = protected,
                    supportBonus = bonus,
                )
            }

            ArchiveUiState(
                items = items,
                totalCount = definitions.size,
                discoveredCount = gameState.discoveredAnimalIds.size,
                protectedCount = gameState.protectedAnimals.size,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ArchiveUiState(),
        )
}
