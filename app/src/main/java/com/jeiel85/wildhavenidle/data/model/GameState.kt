package com.jeiel85.wildhavenidle.data.model

data class GameState(
    val carePoint: Double = 0.0,
    val sanctuaryLevel: Int = 1,
    val lastSavedAt: Long = System.currentTimeMillis(),
    val protectedAnimals: List<ProtectedAnimal> = emptyList(),
    val discoveredAnimalIds: Set<String> = emptySet(),
    val unlockedHabitatIds: Set<String> = setOf("forest_001"),
    val onboardingCompleted: Boolean = false,
    /**
     * 마지막 일일 보너스 수령 시각 (epoch millis). null이면 아직 수령한 적이 없음.
     * 자격 판정은 시각이 아니라 *로컬 자정 경계로 나뉜 달력 날짜* 기준.
     */
    val lastDailyBonusClaimedAtMillis: Long? = null,
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
            lastDailyBonusClaimedAtMillis = null,
        )
    }
}
