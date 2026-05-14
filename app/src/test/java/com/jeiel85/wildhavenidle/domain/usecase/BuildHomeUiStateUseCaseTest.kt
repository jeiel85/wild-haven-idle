package com.jeiel85.wildhavenidle.domain.usecase

import com.jeiel85.wildhavenidle.data.model.GameState
import com.jeiel85.wildhavenidle.data.model.ProtectedAnimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BuildHomeUiStateUseCaseTest {

    private val useCase = BuildHomeUiStateUseCase()

    private fun freshState(): GameState = GameState(
        carePoint = 0.0,
        sanctuaryLevel = 1,
        lastSavedAt = 0L,
        protectedAnimals = listOf(
            ProtectedAnimal(animalId = "rabbit_001", recoveryStage = 1, protectedAt = 0L),
        ),
        discoveredAnimalIds = setOf("rabbit_001"),
        unlockedHabitatIds = setOf("forest_001"),
        onboardingCompleted = false,
    )

    @Test
    fun showOnboardingIsTrueForFreshState() {
        val state = freshState()

        val ui = useCase(gameState = state, offlineReward = null)

        assertTrue(ui.showOnboarding)
    }

    @Test
    fun showOnboardingIsFalseAfterCompletion() {
        val state = freshState().copy(onboardingCompleted = true)

        val ui = useCase(gameState = state, offlineReward = null)

        assertFalse(ui.showOnboarding)
    }

    @Test
    fun productionAndCountsArePropagated() {
        val state = freshState()

        val ui = useCase(gameState = state, offlineReward = null)

        assertEquals(1, ui.protectedAnimalCount)
        assertEquals(1, ui.discoveredAnimalCount)
        assertEquals(5, ui.totalAnimalCount)
        assertTrue(ui.productionPerSecond > 0.0)
    }
}
