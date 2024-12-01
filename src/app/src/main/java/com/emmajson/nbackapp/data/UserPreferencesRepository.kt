package com.emmajson.nbackapp.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.emmajson.nbackapp.ui.viewmodels.GameState
import com.emmajson.nbackapp.ui.viewmodels.GameType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val HIGHSCORE = intPreferencesKey("highscore")

        val GAME_TYPE_KEY = intPreferencesKey("game_type")
        val GAME_LENGTH_KEY = intPreferencesKey("game_length")
        val GRID_SIZE_KEY = intPreferencesKey("grid_size")
        val NBACK_LEVEL_KEY = intPreferencesKey("nback_level")
        val ROUND_DURATION_KEY = longPreferencesKey("round_duration")

        const val TAG = "UserPreferencesRepo"
    }

    // Load highscore as Flow
    val highscore: Flow<Int> = dataStore.data
        .map { preferences -> preferences[HIGHSCORE] ?: 0 }

    // Load settings as Flow<GameState>
    val settings: Flow<GameState> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading settings: ${exception.localizedMessage}")
                emit(emptyPreferences()) // Fallback to empty if read fails
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val gameState = GameState(
                gameType = GameType.values()[preferences[GAME_TYPE_KEY] ?: GameType.AudioVisual.ordinal],
                gameLength = preferences[GAME_LENGTH_KEY] ?: 10,
                gameGridSize = preferences[GRID_SIZE_KEY] ?: 3,
                gameNBackLvl = preferences[NBACK_LEVEL_KEY] ?: 1,
                rondDuration = preferences[ROUND_DURATION_KEY] ?: 2000L
            )
            Log.d(TAG, "Retrieved settings: $gameState")
            gameState
        }

    // Save high score
    suspend fun saveHighScore(score: Int) {
        dataStore.edit { preferences ->
            preferences[HIGHSCORE] = score
        }
    }

    // Save all settings
    suspend fun saveSettings(gameState: GameState) {
        try {
            dataStore.edit { preferences ->
                preferences[GAME_TYPE_KEY] = gameState.gameType.ordinal
                preferences[GAME_LENGTH_KEY] = gameState.gameLength
                preferences[GRID_SIZE_KEY] = gameState.gameGridSize
                preferences[NBACK_LEVEL_KEY] = gameState.gameNBackLvl
                preferences[ROUND_DURATION_KEY] = gameState.rondDuration
            }
            Log.d(TAG, "Settings saved successfully")
        } catch (e: IOException) {
            Log.e(TAG, "Failed to save settings: ${e.localizedMessage}")
        }
    }

}
