package com.jeiel85.wildhavenidle.data.model

data class GameState(
    val carePoint: Double = 0.0,
    val sanctuaryLevel: Int = 1,
    val lastSavedAt: Long = System.currentTimeMillis(),
    val protectedAnimals: List<ProtectedAnimal> = emptyList(),
    val discoveredAnimalIds: Set<String> = emptySet(),
    val unlockedHabitatIds: Set<String> = setOf("forest_001"),
    val onboardingCompleted: Boolean = false,
) {
    companion object {
        fun initial(now: Long): GameState = GameState(
            lastSavedAt = now,
            protectedAnimals = listOf(
                ProtectedAnimal(
                    animalId = "rabbit_001",
                    recoveryStage = 1,
                    protectedAt = now,
                ),
            ),
            discoveredAnimalIds = setOf("rabbit_001"),
            onboardingCompleted = false,
        )
    }
}
