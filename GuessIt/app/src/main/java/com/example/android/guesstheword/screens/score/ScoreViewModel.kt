package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(val finalScore: Int) : ViewModel() {
    companion object {
        private const val TAG = "ScoreViewModel"
    }

    init {
        Log.i(TAG, ": Final score: $finalScore")
    }
}