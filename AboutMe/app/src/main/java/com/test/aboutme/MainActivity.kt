package com.test.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.test.aboutme.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private val myName: MyName = MyName("John")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.myName = myName
        activityMainBinding.mainDoneBt.setOnClickListener { updateNicknameTextView(it) }

        Handler().postDelayed({
            myName.name = "John 2"
            Toast.makeText(this, "update", Toast.LENGTH_SHORT).show()
        },5000)
        
    }

    private fun updateNicknameTextView(view: View) {
        val userSetNickname: String = activityMainBinding.mainNickNameEt.text.toString().trim()
        if (userSetNickname.isEmpty()) {
            Toast.makeText(this, "Please set a valid nickname", Toast.LENGTH_SHORT).show()
            return
        }
        activityMainBinding.apply {
            invalidateAll()
            mainDoneBt.visibility = View.GONE
            mainNickNameEt.visibility = View.GONE
            myName?.nickName = userSetNickname
        }

        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

        activityMainBinding.mainNickNameTv.visibility = View.VISIBLE
        Toast.makeText(this, "Nickname set to $userSetNickname", Toast.LENGTH_SHORT).show()
    }
}