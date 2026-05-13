package com.jeiel85.wildhavenidle.presentation.animals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeiel85.wildhavenidle.data.repository.GameRepository
import com.jeiel85.wildhavenidle.domain.balance.BalanceCalculator
import com.jeiel85.wildhavenidle.domain.definitions.AnimalDefinitions
import com.jeiel85.wildhavenidle.domain.usecase.SupportAnimalRecoveryUseCase
import com.jeiel85.wildhavenidle.domain.usecase.UpgradeSanctuaryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnimalListViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {
    private val supportUseCase = SupportAnimalRecoveryUseCase()
    private val upgradeUseCase = UpgradeSanctuaryUseCase()

    private val lastTransactionResult = MutableStateFlow<TransactionResult?>(null)

    val uiState = combine(
        gameRepository.gameState,
        lastTransactionResult,
    ) { gameState, result ->
        val definitions = AnimalDefinitions.mvpAnimals
        val definitionsById = definitions.associateBy { it.id }
        val productionPerSecond = BalanceCalculator.calculateTotalProductionPerSecond(
            sanctuaryLevel = gameState.sanctuaryLevel,
            protectedAnimals = gameState.protectedAnimals,
            definitions = definitions,
        )

        val animalItems = definitions.mapNotNull { def ->
            val protected = gameState.protectedAnimals.firstOrNull { it.animalId == def.id }
            if (protected != null) {
                AnimalListItem(
                    definition = def,
                    protectedAnimal = protected,
                    recoveryCost = supportUseCase.getCost(def, protected.recoveryStage),
                    supportBonus = BalanceCalculator.calculateAnimalSupportBonus(def, protected.recoveryStage),
                )
            } else {
                null
            }
        }

        AnimalListUiState(
            carePoint = gameState.carePoint,
            sanctuaryLevel = gameState.sanctuaryLevel,
            sanctuaryUpgradeCost = upgradeUseCase.getCost(gameState.sanctuaryLevel),
            productionPerSecond = productionPerSecond,
            animals = animalItems,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AnimalListUiState(),
    )

    fun supportRecovery(animalId: String) {
        viewModelScope.launch {
            val cost = gameRepository.supportAnimalRecovery(animalId)
            lastTransactionResult.value = if (cost > 0L) {
                TransactionResult.Success
            } else {
                TransactionResult.Error
            }
        }
    }

    fun upgradeSanctuary() {
        viewModelScope.launch {
            val cost = gameRepository.upgradeSanctuary()
            lastTransactionResult.value = if (cost > 0L) {
                TransactionResult.Success
            } else {
                TransactionResult.Error
            }
        }
    }

    fun clearTransactionResult() {
        lastTransactionResult.value = null
    }
}

sealed class TransactionResult {
    data object Success : TransactionResult()
    data object Error : TransactionResult()
}
