package com.emmajson.nbackapp.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.emmajson.nbackapp.GameApplication
import com.emmajson.nbackapp.NBackHelper

import android.speech.tts.TextToSpeech
import java.util.Locale
import com.emmajson.nbackapp.data.UserPreferencesRepository
import kotlinx.coroutines.coroutineScope

interface GameViewModel {
    val gameState: StateFlow<GameState>
    val score: StateFlow<Int>
    val highscore: StateFlow<Int>
    val currentIndex: StateFlow<Int>

    fun setGameType(gameType: GameType)
    fun setGameLength(gameLength: Int)
    fun setNBack(gameNBackLvl: Int)
    fun setGridSize(gameGridSize: Int)
    fun setDuration(millis: Long)

    suspend fun saveCurrentSettings()


    fun startGame()
    fun stopGame()

    fun checkMatchPlacement(currentIndex: Int): Boolean
    fun checkMatchAudio(currentIndex: Int): Boolean
}

class GameVM(
    private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository
): GameViewModel, ViewModel(), TextToSpeech.OnInitListener {
    private val _gameState = MutableStateFlow(GameState())
    override val gameState: StateFlow<GameState>
        get() = _gameState.asStateFlow()

    private val _score = MutableStateFlow(0)
    override val score: StateFlow<Int>
        get() = _score

    private val _highscore = MutableStateFlow(0)
    override val highscore: StateFlow<Int>
        get() = _highscore

    private val _currentIndex = MutableStateFlow(0)
    override val currentIndex: StateFlow<Int>
        get() = _currentIndex.asStateFlow()


    private val matchingEventsPlacement = mutableSetOf<Int>() // Track unique match positions /
    private val matchingEventsAudio = mutableSetOf<Int>() // Track unique match positions

    private var job: Job? = null  // coroutine job for the game event

    private val nBackHelper = NBackHelper()  // Helper that generate the event array
    private var eventsPlacement = emptyArray<Int>()  // Array with all events
    private var eventsAudio = emptyArray<Char>()  // Array with all events


    /////////TODO TTS
    private lateinit var tts: TextToSpeech

    init {
        // Initialize TTS
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US // Set language to English (US)
        } else {
            Log.e("GameVM", "Text-to-Speech initialization failed.")
        }
    }

    override fun onCleared() {
        // Shutdown TTS to free up resources
        tts.stop()
        tts.shutdown()
        super.onCleared()
    }
    ////////////////////////////////TODO

    override fun setGameType(gameType: GameType) {
        // update the gametype in the gamestate
        _gameState.value = _gameState.value.copy(gameType = gameType)
    }

    override fun setGameLength(gameLength: Int) {
        // update the gametype in the gamestate
        _gameState.value = _gameState.value.copy(gameLength = gameLength)
    }
    override fun setNBack(gameNBackLvl: Int) {
        // update the gametype in the gamestate
        _gameState.value = _gameState.value.copy(gameNBackLvl = gameNBackLvl)
    }
    override fun setGridSize(gameGridSize: Int) {
        _gameState.value = _gameState.value.copy(gameGridSize = gameGridSize)
    }
    override fun setDuration(millis: Long) {
        _gameState.value = _gameState.value.copy(rondDuration = millis)
    }

    override fun startGame() {
        job?.cancel()  // Cancel any existing game loop

        matchingEventsPlacement.clear()
        matchingEventsAudio.clear()

        // Reset game state and event value for a fresh start
        _gameState.value = _gameState.value.copy(eventValue = -1, eventAudioValue = ' ')
        _score.value = 0  // Reset score if needed

        // Fetch and log events from the helper
        eventsPlacement = nBackHelper.generateNBackString(_gameState.value.gameLength, (_gameState.value.gameGridSize* _gameState.value.gameGridSize), 25, _gameState.value.gameNBackLvl).toList().toTypedArray()
        eventsAudio = nBackHelper.generateNBackString(_gameState.value.gameLength, 25, 25, _gameState.value.gameNBackLvl).map { 'A' + it }.toTypedArray()

        job = viewModelScope.launch {
            when (gameState.value.gameType) {
                GameType.Audio -> runAudioGame(eventsAudio)
                GameType.AudioVisual -> runAudioVisualGame(eventsAudio, eventsPlacement)
                GameType.Visual -> runVisualGame(eventsPlacement)
            }

            if (_score.value > _highscore.value) {
                _highscore.value = _score.value
                userPreferencesRepository.saveHighScore(_highscore.value) // Save new high score
                Log.d("GameVM", "New high score saved: ${_highscore.value}")
            }
        }
    }

    override fun stopGame() {
        job?.cancel()  // Cancel any existing game loop
        gameState.value.eventValue = -2
    }

    override fun checkMatchPlacement(currentIndex: Int): Boolean {
        return if (currentIndex >= _gameState.value.gameNBackLvl && eventsPlacement[currentIndex] == eventsPlacement[currentIndex - _gameState.value.gameNBackLvl]) {
            if (matchingEventsPlacement.add(currentIndex)) { // Register match only if not already registered
                _score.value += 1 // Increment score
                println("Match confirmed at position $currentIndex, score incremented.")
                true  // Return true as a match was scored
            } else {
                println("Match at position $currentIndex was already registered.")
                false  // Return false as this match was already registered
            }
        } else {
            println("No match at position $currentIndex.")
            false  // Return false as there was no match
        }
    }

    override fun checkMatchAudio(currentIndex: Int): Boolean {
        return if (currentIndex >= _gameState.value.gameNBackLvl && eventsAudio[currentIndex] == eventsAudio[currentIndex - _gameState.value.gameNBackLvl]) {
            if (matchingEventsAudio.add(currentIndex)) { // Register match only if not already registered
                _score.value += 1 // Increment score
                println("Match confirmed at position $currentIndex, score incremented.")
                true
            } else {
                println("Match at position $currentIndex was already registered.")
                false
            }
        } else {
            println("No match at position $currentIndex.")
            false
        }.also {
            println("Matching events so far: $matchingEventsAudio")
        }
    }

    private suspend fun runAudioGame(eventsAudio: Array<Char>) {
        for ((index, value) in eventsAudio.withIndex()) {
            _gameState.value = _gameState.value.copy(eventAudioValue = value)
            _currentIndex.value = index // Update currentIndex correctly

            // Log the current state to debug
            Log.d("GameVM", "Current Index: $index, Event Value: $value")
            tts?.speak(value.toString(), TextToSpeech.QUEUE_FLUSH, null, "eventAudioValue_$index")
            if (gameState.value.eventValue == -2) {
                tts?.shutdown()
            }
            delay(_gameState.value.rondDuration)
        }
    }

    private suspend fun runVisualGame(eventsPlacement: Array<Int>) {
        // Ensure the first value is emitted before starting the loop
        if (eventsPlacement.isNotEmpty()) {
            _gameState.value = _gameState.value.copy(eventValue = eventsPlacement[0])
            _currentIndex.value = 0
            delay(_gameState.value.rondDuration) // Allow time for UI to display this first state
        }

        // Start loop at the second item to avoid re-emitting the first
        for (index in 1 until eventsPlacement.size) {
            _gameState.value = _gameState.value.copy(eventValue = eventsPlacement[index])
            _currentIndex.value = index

            // Log the current state to debug
            Log.d("GameVM", "Current Index: $index, Event Value: ${eventsPlacement[index]}")
            delay(_gameState.value.rondDuration)
        }
    }

    private suspend fun runAudioVisualGame(eventsAudio: Array<Char>, eventsPlacement: Array<Int>) {
        coroutineScope {
            // Launch the visual game in a separate coroutine
            launch {
                runVisualGame(eventsPlacement)
            }

            // Launch the audio game in a separate coroutine
            launch {
                runAudioGame(eventsAudio)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
                GameVM(application, application.userPreferencesRespository)
            }
        }
    }

    init {
        // Launch separate coroutines for highscore and settings to avoid blocking
        viewModelScope.launch {
            userPreferencesRepository.highscore.collect { score ->
                _highscore.value = score
                Log.d("GameVM", "Loaded highscore: $score")
            }
        }

        viewModelScope.launch {
            userPreferencesRepository.settings.collect { loadedSettings ->
                _gameState.value = loadedSettings
                Log.d("GameVM", "Loaded settings on app start: $loadedSettings")
            }
        }
    }

    // Save settings to repository
    override suspend fun saveCurrentSettings() {
        userPreferencesRepository.saveSettings(_gameState.value)
        Log.d("GameVM", "Saving current settings: ${_gameState.value}")
    }


}

// Class with the different game types
enum class GameType{
    Audio,
    Visual,
    AudioVisual;
}

data class GameState(
    // You can use this state to push values from the VM to your UI.
    val gameType: GameType = GameType.AudioVisual,  // Type of the game
    val gameLength: Int = 10,
    val gameNBackLvl: Int = 1,
    val gameGridSize: Int = 3,
    val rondDuration: Long = 2000,
    var eventValue: Int = -1,  // The value of the array string
    val eventAudioValue: Char = ' '
)

class FakeVM: GameViewModel{
    override val gameState: StateFlow<GameState>
        get() = MutableStateFlow(GameState()).asStateFlow()
    override val score: StateFlow<Int>
        get() = MutableStateFlow(2).asStateFlow()
    override val highscore: StateFlow<Int>
        get() = MutableStateFlow(42).asStateFlow()
    override val currentIndex: StateFlow<Int>
        get() = MutableStateFlow(0).asStateFlow()


    override fun setGameType(gameType: GameType) {
    }
    override fun setGameLength(gameLength: Int) {
    }
    override fun setNBack(gameNBackLvl: Int) {
    }
    override fun setGridSize(gameGridSize: Int) {
    }
    override fun setDuration(millis: Long) {
    }

    override suspend fun saveCurrentSettings() {
        TODO("Not yet implemented")
    }


    override fun startGame() {
    }

    override fun stopGame() {
    }

    override fun checkMatchPlacement(currentIndex: Int): Boolean {
        return false
    }
    override fun checkMatchAudio(currentIndex: Int): Boolean {
        return false
    }
}