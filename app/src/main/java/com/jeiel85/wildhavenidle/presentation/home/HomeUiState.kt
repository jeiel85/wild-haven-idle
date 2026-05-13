package com.jeiel85.wildhavenidle.presentation.home

data class HomeUiState(
    val carePoint: Double,
    val sanctuaryLevel: Int,
    val productionPerSecond: Double,
    val protectedAnimalCount: Int,
    val discoveredAnimalCount: Int,
    val totalAnimalCount: Int,
)
