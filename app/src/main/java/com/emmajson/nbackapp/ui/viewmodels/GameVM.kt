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

/**
 * This is the GameViewModel.
 *
 * It is good practice to first make an interface, which acts as the blueprint
 * for your implementation. With this interface we can create fake versions
 * of the viewmodel, which we can use to test other parts of our app that depend on the VM.
 *
 * Our viewmodel itself has functions to start a game, to specify a gametype,
 * and to check if we are having a match
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */


interface GameViewModel {
    val gameState: StateFlow<GameState>
    val score: StateFlow<Int>
    val highscore: StateFlow<Int>
    val nBack: Int
    val currentIndex: StateFlow<Int>

    fun setGameType(gameType: GameType)
    fun startGame()

    fun checkMatchPlacement(currentIndex: Int)
    fun checkMatchAudio(currentIndex: Int)
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

    // nBack is currently hardcoded
    override val nBack: Int = 1

    private val _currentIndex = MutableStateFlow(0)
    override val currentIndex: StateFlow<Int>
        get() = _currentIndex.asStateFlow()


    private val matchingEventsPlacement = mutableSetOf<Int>() // Track unique match positions // TODO : ADDED THIS
    private val matchingEventsAudio = mutableSetOf<Int>() // Track unique match positions // TODO : ADDED THIS

    private var job: Job? = null  // coroutine job for the game event
    private val eventInterval: Long = 2000L  // 2000 ms (2s)

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

    override fun startGame() {
        job?.cancel()  // Cancel any existing game loop

        matchingEventsPlacement.clear()
        matchingEventsAudio.clear()

        // Get the events from our C-model (returns IntArray, so we need to convert to Array<Int>)
        eventsPlacement = nBackHelper.generateNBackString(20, 9, 30, nBack).toList().toTypedArray()  // Todo Higher Grade: currently the size etc. are hardcoded, make these based on user input
        Log.d("GameVM", "The following sequence was generated: ${eventsPlacement.contentToString()}")

        val intArray = nBackHelper.generateNBackString(20, 25, 30, nBack).toList().toTypedArray()

        // Generate character events by converting integers to characters
        eventsAudio = intArray.map { 'A' + it }.toTypedArray()
        Log.d("GameVM", "The following character sequence was generated: ${eventsAudio.contentToString()}")


        // Reset score if needed
        _score.value = 0

        job = viewModelScope.launch {
            when (gameState.value.gameType) {
                GameType.Audio -> runAudioGame(eventsAudio)
                GameType.AudioVisual -> runAudioVisualGame(eventsAudio, eventsPlacement)
                GameType.Visual -> runVisualGame(eventsPlacement)
            }
            // After the round ends, check if the current score is a new high score
            if (_score.value > _highscore.value) {
                _highscore.value = _score.value
                userPreferencesRepository.saveHighScore(_highscore.value) // Save the new high score
                Log.d("GameVM", "New high score saved: ${_highscore.value}")
            }
        }
    }


    override fun checkMatchPlacement(currentIndex: Int) {
        if (currentIndex >= nBack && eventsPlacement[currentIndex] == eventsPlacement[currentIndex - nBack]) {
            if (matchingEventsPlacement.add(currentIndex)) { // Register match only if not already registered
                _score.value += 1 // Increment score
                println("Match confirmed at position $currentIndex, score incremented.")
            } else {
                println("Match at position $currentIndex was already registered.")
            }
        } else {
            println("No match at position $currentIndex.")
        }
        println("Matching events so far: $matchingEventsPlacement")
    }

    override fun checkMatchAudio(currentIndex: Int) {
        if (currentIndex >= nBack && eventsAudio[currentIndex] == eventsAudio[currentIndex - nBack]) {
            if (matchingEventsAudio.add(currentIndex)) { // Register match only if not already registered
                _score.value += 1 // Increment score
                println("Match confirmed at position $currentIndex, score incremented.")
            } else {
                println("Match at position $currentIndex was already registered.")
            }
        } else {
            println("No match at position $currentIndex.")
        }
        println("Matching events so far: $matchingEventsAudio")
    }


    private suspend fun runAudioGame(eventsAudio: Array<Char>) {
        // Todo: Make work for Basic grade
        for ((index, value) in eventsAudio.withIndex()) {
            _gameState.value = _gameState.value.copy(eventAudioValue = value)
            _currentIndex.value = index // Update currentIndex correctly

            // Log the current state to debug
            Log.d("GameVM", "Current Index: $index, Event Value: $value")
            tts?.speak(value.toString(), TextToSpeech.QUEUE_FLUSH, null, "eventAudioValue_$index")

            delay(eventInterval)
        }
    }

    private suspend fun runVisualGame(eventsPlacement: Array<Int>) {
        for ((index, value) in eventsPlacement.withIndex()) {
            _gameState.value = _gameState.value.copy(eventValue = value)
            _currentIndex.value = index // Update currentIndex correctly

            // Log the current state to debug
            Log.d("GameVM", "Current Index: $index, Event Value: $value")

            delay(eventInterval)
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
        // Code that runs during creation of the vm
        viewModelScope.launch {
            userPreferencesRepository.highscore.collect {
                _highscore.value = it
            }
        }
    }
}

// Class with the different game types
enum class GameType{
    Audio,
    Visual,
    AudioVisual
}

data class GameState(
    // You can use this state to push values from the VM to your UI.
    val gameType: GameType = GameType.AudioVisual,  // Type of the game
    val eventValue: Int = -1,  // The value of the array string
    val eventAudioValue: Char = ' '
)

class FakeVM: GameViewModel{
    override val gameState: StateFlow<GameState>
        get() = MutableStateFlow(GameState()).asStateFlow()
    override val score: StateFlow<Int>
        get() = MutableStateFlow(2).asStateFlow()
    override val highscore: StateFlow<Int>
        get() = MutableStateFlow(42).asStateFlow()
    override val nBack: Int
        get() = 2

    override val currentIndex: StateFlow<Int>
        get() = MutableStateFlow(0).asStateFlow()


    override fun setGameType(gameType: GameType) {
    }

    override fun startGame() {
    }

    override fun checkMatchPlacement(currentIndex: Int) {
    }
    override fun checkMatchAudio(currentIndex: Int) {
    }
}