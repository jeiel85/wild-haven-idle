package com.jeiel85.wildhavenidle.presentation.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeiel85.wildhavenidle.data.repository.GameRepository

class ArchiveViewModelFactory(
    private val gameRepository: GameRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArchiveViewModel::class.java)) {
            return ArchiveViewModel(gameRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
