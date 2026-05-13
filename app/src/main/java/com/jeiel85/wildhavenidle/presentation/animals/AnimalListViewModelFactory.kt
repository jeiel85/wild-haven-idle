package com.jeiel85.wildhavenidle.presentation.animals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeiel85.wildhavenidle.data.repository.GameRepository

class AnimalListViewModelFactory(
    private val gameRepository: GameRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimalListViewModel::class.java)) {
            return AnimalListViewModel(gameRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
