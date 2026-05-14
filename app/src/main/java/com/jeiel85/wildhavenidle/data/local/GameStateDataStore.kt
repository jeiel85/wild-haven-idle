package com.jeiel85.wildhavenidle.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jeiel85.wildhavenidle.data.model.GameState
import com.jeiel85.wildhavenidle.data.model.ProtectedAnimal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val GAME_STATE_STORE_NAME = "game_state"
private const val LIST_SEPARATOR = "|"
private const val FIELD_SEPARATOR = ":"

private val Context.gameStatePreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = GAME_STATE_STORE_NAME,
)

class GameStateDataStore(
    context: Context,
    private val defaultNowMillis: () -> Long,
) {
    private val dataStore = context.applicationContext.gameStatePreferencesDataStore

    val gameState: Flow<GameState> = dataStore.data
        .catch { error ->
            if (error is IOException) {
                emit(emptyPreferences())
            } else {
                throw error
            }
        }
        .map { preferences ->
            preferences.toGameState(defaultNowMillis())
        }

    suspend fun save(gameState: GameState) {
        dataStore.edit { preferences ->
            preferences[Keys.carePoint] = gameState.carePoint
            preferences[Keys.sanctuaryLevel] = gameState.sanctuaryLevel
            preferences[Keys.lastSavedAt] = gameState.lastSavedAt
            preferences[Keys.protectedAnimals] = encodeProtectedAnimals(gameState.protectedAnimals)
            preferences[Keys.discoveredAnimalIds] = gameState.discoveredAnimalIds.joinToString(LIST_SEPARATOR)
            preferences[Keys.unlockedHabitatIds] = gameState.unlockedHabitatIds.joinToString(LIST_SEPARATOR)
            preferences[Keys.onboardingCompleted] = gameState.onboardingCompleted
        }
    }

    private fun Preferences.toGameState(now: Long): GameState {
        if (!contains(Keys.lastSavedAt)) {
            return GameState.initial(now)
        }

        // 기존 사용자(저장 데이터는 있지만 온보딩 키가 없음)는 이미 게임에 익숙하므로
        // 온보딩을 다시 띄우지 않는다. 신규 키 도입 시 마이그레이션 기본값.
        val onboardingCompleted = this[Keys.onboardingCompleted] ?: true

        return GameState(
            carePoint = this[Keys.carePoint] ?: 0.0,
            sanctuaryLevel = this[Keys.sanctuaryLevel] ?: 1,
            lastSavedAt = this[Keys.lastSavedAt] ?: now,
            protectedAnimals = decodeProtectedAnimals(this[Keys.protectedAnimals].orEmpty()),
            discoveredAnimalIds = decodeStringSet(this[Keys.discoveredAnimalIds]).ifEmpty { setOf("rabbit_001") },
            unlockedHabitatIds = decodeStringSet(this[Keys.unlockedHabitatIds]).ifEmpty { setOf("forest_001") },
            onboardingCompleted = onboardingCompleted,
        )
    }

    private fun encodeProtectedAnimals(protectedAnimals: List<ProtectedAnimal>): String =
        protectedAnimals.joinToString(LIST_SEPARATOR) { animal ->
            listOf(
                animal.animalId,
                animal.recoveryStage.toString(),
                animal.protectedAt.toString(),
            ).joinToString(FIELD_SEPARATOR)
        }

    private fun decodeProtectedAnimals(value: String): List<ProtectedAnimal> =
        value.split(LIST_SEPARATOR)
            .filter { it.isNotBlank() }
            .mapNotNull { encoded ->
                val fields = encoded.split(FIELD_SEPARATOR)
                val animalId = fields.getOrNull(0)?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
                val recoveryStage = fields.getOrNull(1)?.toIntOrNull() ?: return@mapNotNull null
                val protectedAt = fields.getOrNull(2)?.toLongOrNull() ?: return@mapNotNull null

                ProtectedAnimal(
                    animalId = animalId,
                    recoveryStage = recoveryStage,
                    protectedAt = protectedAt,
                )
            }
            .ifEmpty { GameState.initial(defaultNowMillis()).protectedAnimals }

    private fun decodeStringSet(value: String?): Set<String> =
        value.orEmpty()
            .split(LIST_SEPARATOR)
            .filter { it.isNotBlank() }
            .toSet()

    private object Keys {
        val carePoint = doublePreferencesKey("care_point")
        val sanctuaryLevel = intPreferencesKey("sanctuary_level")
        val lastSavedAt = longPreferencesKey("last_saved_at")
        val protectedAnimals = stringPreferencesKey("protected_animals")
        val discoveredAnimalIds = stringPreferencesKey("discovered_animal_ids")
        val unlockedHabitatIds = stringPreferencesKey("unlocked_habitat_ids")
        val onboardingCompleted = booleanPreferencesKey("onboarding_completed")
    }
}
