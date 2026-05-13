package com.jeiel85.wildhavenidle.presentation.home

data class HomeUiState(
    val carePoint: Double = 0.0,
    val sanctuaryLevel: Int = 1,
    val productionPerSecond: Double = 0.0,
    val protectedAnimalCount: Int = 0,
    val discoveredAnimalCount: Int = 0,
    val totalAnimalCount: Int = 0,
    val offlineReward: Double? = null,
)
