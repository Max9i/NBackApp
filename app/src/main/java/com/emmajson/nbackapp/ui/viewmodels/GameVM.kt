package com.emmajson.nbackapp.ui.viewmodels

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
import com.emmajson.nbackapp.data.UserPreferencesRepository

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

    fun checkMatch(currentIndex: Int)
}

class GameVM(
    private val userPreferencesRepository: UserPreferencesRepository
): GameViewModel, ViewModel() {
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


    private val matchingEvents = mutableSetOf<Int>() // Track unique match positions // TODO : ADDED THIS


    private var job: Job? = null  // coroutine job for the game event
    private val eventInterval: Long = 2000L  // 2000 ms (2s)

    private val nBackHelper = NBackHelper()  // Helper that generate the event array
    private var events = emptyArray<Int>()  // Array with all events

    override fun setGameType(gameType: GameType) {
        // update the gametype in the gamestate
        _gameState.value = _gameState.value.copy(gameType = gameType)
    }

    override fun startGame() {
        job?.cancel()  // Cancel any existing game loop

        matchingEvents.clear()

        // Get the events from our C-model (returns IntArray, so we need to convert to Array<Int>)
        events = nBackHelper.generateNBackString(10, 9, 30, nBack).toList().toTypedArray()  // Todo Higher Grade: currently the size etc. are hardcoded, make these based on user input
        Log.d("GameVM", "The following sequence was generated: ${events.contentToString()}")

        // Reset score if needed
        _score.value = 0

        job = viewModelScope.launch {
            when (gameState.value.gameType) {
                GameType.Audio -> runAudioGame()
                GameType.AudioVisual -> runAudioVisualGame()
                GameType.Visual -> runVisualGame(events)
            }
            // Todo: update the highscore
            // After the round ends, check if the current score is a new high score
            if (_score.value > _highscore.value) {
                _highscore.value = _score.value
                userPreferencesRepository.saveHighScore(_highscore.value) // Save the new high score
                Log.d("GameVM", "New high score saved: ${_highscore.value}")
            }
        }
    }



    override fun checkMatch(currentIndex: Int) {
        if (currentIndex >= nBack && events[currentIndex] == events[currentIndex - nBack]) {
            if (matchingEvents.add(currentIndex)) { // Register match only if not already registered
                _score.value += 1 // Increment score
                println("Match confirmed at position $currentIndex, score incremented.")
            } else {
                println("Match at position $currentIndex was already registered.")
            }
        } else {
            println("No match at position $currentIndex.")
        }
        println("Matching events so far: $matchingEvents")
    }


    private fun runAudioGame() {
        // Todo: Make work for Basic grade
    }

    private suspend fun runVisualGame(events: Array<Int>) {
        for ((index, value) in events.withIndex()) {
            _gameState.value = _gameState.value.copy(eventValue = value)
            _currentIndex.value = index // Update currentIndex correctly

            // Log the current state to debug
            Log.d("GameVM", "Current Index: $index, Event Value: $value")

            delay(eventInterval)
        }
    }

    private fun runAudioVisualGame(){
        // Todo: Make work for Higher grade
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
                GameVM(application.userPreferencesRespository)
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
    val gameType: GameType = GameType.Visual,  // Type of the game
    val eventValue: Int = -1  // The value of the array string
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

    override fun checkMatch(currentIndex: Int) {
    }
}