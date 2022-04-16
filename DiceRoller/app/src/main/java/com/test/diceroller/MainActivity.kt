package com.test.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.test.diceroller.databinding.ActivityMainBinding
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    companion object {
        val TAG:String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(activityMainBinding.root)
        activityMainBinding.rollButton.text = "Let's roll"
        activityMainBinding.rollButton.setOnClickListener {rollDice()}
    }

    private fun rollDice() {

        // a = 3%
        // b = 7%
        // c = 50%
        // d = 20%
        // e = 20%

        val sortedValue: Double = Math.random()
        val sortedLetterIs: Char = when {
            sortedValue < 0.03 -> 'a'
            sortedValue < 0.10 -> 'b'
            sortedValue < 0.60 -> 'c'
            sortedValue < 0.80 -> 'd'
            else -> 'e'
        }

        Log.e(TAG, "rollDice: $sortedValue $sortedLetterIs")


        val rolledNumber: Int = Random.nextInt(1,7)
        activityMainBinding.diceImage.setImageResource(when (rolledNumber) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.empty_dice
        })
    }
}