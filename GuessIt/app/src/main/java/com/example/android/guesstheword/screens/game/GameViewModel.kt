package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel(private val listener: GameFinishListener) : ViewModel() {
    companion object {
        private const val TAG:String = "GameViewModel"

        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 10000L

        private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
        private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
        private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
        private val NO_BUZZ_PATTERN = longArrayOf(0)
    }

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    // The current word
    private val _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    // Notifies that the game is finishing to GameFragment
    private val _eventGameFinishing = MutableLiveData<Boolean>()
    val eventGameFinishing: LiveData<Boolean>
        get() = _eventGameFinishing

    private val _elapsedTime = MutableLiveData<String>()
    val elapsedTime : LiveData<String>
        get() = _elapsedTime

    private val timerCounter = MutableLiveData<Long>()
    val elapsedTimeInString = Transformations.map(timerCounter) { currentTime ->
        DateUtils.formatElapsedTime(currentTime)
    }

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val timer : CountDownTimer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND/100) {
        override fun onTick(millisUntilFinished: Long) {
            _elapsedTime.postValue(DateUtils.formatElapsedTime(millisUntilFinished))
            timerCounter.postValue(millisUntilFinished)
        }

        override fun onFinish() {
            _eventGameFinishing.postValue(true)
            listener.onGameFinished()
        }
    }

    init {
        Log.i(TAG, "init: View model created")
        resetList()
        nextWord()
        _score.value = 0
        _elapsedTime.value = "0:00"
        timer.start()
    }

    override fun onCleared() {
        timer.cancel()
        super.onCleared()
        Log.i(TAG, "onCleared: onCleared")
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)

    }

    /** Methods for buttons presses **/
    fun onSkip() {
        _score.value = _score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = _score.value?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinishing.value = false
    }

    interface GameFinishListener {
        fun onGameFinished()
    }
}