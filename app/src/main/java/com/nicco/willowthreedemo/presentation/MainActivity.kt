package com.nicco.willowthreedemo.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nicco.willowthreedemo.databinding.ActivityMainBinding

const val IS_TIMED_MODE = "IS_TIMED_MODE"

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnPracticeMode.setOnClickListener {
            startGameByType()
        }

        binding.btnTimedMode.setOnClickListener {
            startGameByType(true)
        }
    }

    private fun startGameByType(isTimedMode: Boolean = false) {
        val intent = Intent(this, GameActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean(IS_TIMED_MODE, isTimedMode)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}

