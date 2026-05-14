package com.jeiel85.wildhavenidle.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeiel85.wildhavenidle.data.repository.GameRepository
import com.jeiel85.wildhavenidle.domain.usecase.BuildHomeUiStateUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TICK_MILLIS = 1_000L

class HomeViewModel(
    private val gameRepository: GameRepository,
    private val buildHomeUiState: BuildHomeUiStateUseCase = BuildHomeUiStateUseCase(),
) : ViewModel() {
    private val offlineReward = MutableStateFlow<Double?>(null)

    val uiState: StateFlow<HomeUiState> = combine(
        gameRepository.gameState,
        offlineReward,
    ) { gameState, reward ->
        buildHomeUiState(gameState, reward)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeUiState(),
    )

    init {
        viewModelScope.launch {
            val reward = gameRepository.applyOfflineRewardIfNeeded()
            if (reward > 0.0) {
                offlineReward.value = reward
            }

            while (true) {
                delay(TICK_MILLIS)
                gameRepository.addCarePoint(uiState.value.productionPerSecond)
            }
        }
    }

    fun clearOfflineReward() {
        offlineReward.value = null
    }

    fun upgradeSanctuary() {
        viewModelScope.launch {
            gameRepository.upgradeSanctuary()
        }
    }

    fun supportRecovery(animalId: String) {
        viewModelScope.launch {
            gameRepository.supportAnimalRecovery(animalId)
        }
    }

    fun tapSanctuary(): Double {
        val reward = uiState.value.tapReward
        viewModelScope.launch {
            gameRepository.addCarePoint(reward)
        }
        return reward
    }
}
