package com.jeiel85.wildhavenidle.domain.usecase

import com.jeiel85.wildhavenidle.data.model.GameState
import com.jeiel85.wildhavenidle.data.model.ProtectedAnimal
import com.jeiel85.wildhavenidle.presentation.home.RecommendedAction
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

    @Test
    fun recommendsWaitWhenNothingAffordable() {
        // 신규 상태 (carePoint=0)에서는 보호구역 확장(200)도, 회복도 못 함.
        val state = freshState()

        val ui = useCase(gameState = state, offlineReward = null)

        val action = ui.recommendedAction
        assertTrue("기대: WaitForNext, 실제: $action", action is RecommendedAction.WaitForNext)
        action as RecommendedAction.WaitForNext
        assertTrue("대기 시간이 양수여야 함", action.secondsUntilNext > 0L)
    }

    @Test
    fun recommendsSanctuaryUpgradeWhenAffordable() {
        // 보호구역 확장 비용(200)을 보유하면 무조건 확장이 우선.
        val state = freshState().copy(carePoint = 250.0)

        val ui = useCase(gameState = state, offlineReward = null)

        val action = ui.recommendedAction
        assertTrue("기대: UpgradeSanctuary, 실제: $action", action is RecommendedAction.UpgradeSanctuary)
        action as RecommendedAction.UpgradeSanctuary
        assertEquals(2, action.nextLevel)
    }

    @Test
    fun recommendsRecoveryWhenUpgradeUnaffordableButRecoveryAvailable() {
        // 확장은 못 하지만 (carePoint < 200) 회복 비용은 충분한 상황을 만든다.
        // rabbit recovery base cost는 100, stage 1 → cost 약 100~120
        val state = freshState().copy(carePoint = 150.0)

        val ui = useCase(gameState = state, offlineReward = null)

        val action = ui.recommendedAction
        assertTrue("기대: SupportRecovery, 실제: $action", action is RecommendedAction.SupportRecovery)
        action as RecommendedAction.SupportRecovery
        assertEquals("rabbit_001", action.animalId)
        assertEquals(2, action.nextStage)
    }

    @Test
    fun upgradeBeatsRecoveryWhenBothAffordable() {
        // 둘 다 가능하면 확장 우선.
        val state = freshState().copy(carePoint = 1_000.0)

        val ui = useCase(gameState = state, offlineReward = null)

        assertTrue(ui.recommendedAction is RecommendedAction.UpgradeSanctuary)
    }
}
