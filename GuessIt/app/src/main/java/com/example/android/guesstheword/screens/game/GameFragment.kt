/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment(), GameViewModel.GameFinishListener {

    companion object {
        private const val TAG: String = "GameFragment"
    }

    private lateinit var binding: GameFragmentBinding

    private lateinit var gameFragmentViewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_fragment,
            container,
            false
        )

        Log.i(TAG, "onCreateView: get GameViewModel")
        val gameViewModelFactory = GameViewModelFactory(this)
        gameFragmentViewModel = ViewModelProvider(this, gameViewModelFactory).get(GameViewModel::class.java)

        binding.gameViewModel = gameFragmentViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        /* Changed to data binding - Check game_fragment.xml
        // Update the user's score
        gameFragmentViewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })
        // Update the word to guess
        gameFragmentViewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
            binding.wordText.text = newWord ?: ""
        })
        gameFragmentViewModel.elapsedTime.observe(viewLifecycleOwner, Observer { elapsedTime ->
            binding.timerText.text = elapsedTime
        })*/

        // Another way to get the time with Transformations.map on ViewModel
        gameFragmentViewModel.elapsedTimeInString.observe(viewLifecycleOwner, Observer {
            // Log.i(TAG, "onCreateView: current elapsed time: $it")
        })

        // Observe when the game finishes
        /*gameFragmentViewModel.eventGameFinishing.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished) {
                val action = GameFragmentDirections.actionGameToScore(gameFragmentViewModel.score.value ?: 0)
                findNavController(this).navigate(action)
                gameFragmentViewModel.onGameFinishComplete()
            }
        })*/

        return binding.root

    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                buzzer.vibrate(pattern, -1)
            }
        }
    }

    override fun onGameFinished() {
        val action = GameFragmentDirections.actionGameToScore(gameFragmentViewModel.score.value ?: 0)
        findNavController(this).navigate(action)
        gameFragmentViewModel.onGameFinishComplete()
        buzz(GameViewModel.BuzzType.GAME_OVER.pattern)
    }
}
