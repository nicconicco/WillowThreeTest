package com.nicco.willowthreedemo.presentation

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.nicco.willowthreedemo.GameApp
import com.nicco.willowthreedemo.R
import com.nicco.willowthreedemo.data.model.UserResponse
import com.nicco.willowthreedemo.databinding.ActivityGameBinding
import com.nicco.willowthreedemo.framework.util.gone
import com.nicco.willowthreedemo.framework.util.show
import com.nicco.willowthreedemo.presentation.model.UserWillowUi
import com.nicco.willowthreedemo.presentation.viewmodel.GameStateView
import com.nicco.willowthreedemo.presentation.viewmodel.GameViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class GameActivity : AppCompatActivity() {

    private companion object {
        var counterTimer = 0
        const val MAX_INTERACTIONS = 100
        var alertDialogIsNotVisible: Boolean = true
        var builder: AlertDialog.Builder? = null
    }

    private val gameViewModel: GameViewModel by viewModel()

    private val binding: ActivityGameBinding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val toolbarProgress = configureToolbar()

        val isTimedMode = intent.extras?.getBoolean(IS_TIMED_MODE) ?: false

        if (isTimedMode) {
            binding.toolbar.toolbarProgress.show()
            startTimer(toolbarProgress)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            gameViewModel.userList.collect {
                when (it) {
                    is GameStateView.SuccessList -> {
                        binding.progressBar.gone()

                        setupView(it.list)
                    }

                    is GameStateView.Idle -> {
                        binding.progressBar.show()
                        Log.d(GameActivity::class.java.simpleName, "Idle state")
                    }

                    is GameStateView.Error -> {
                        binding.progressBar.gone()
                        showErrorDialog()
                    }

                    is GameStateView.ShowDialogGameEnded -> {
                        alertDialogIsNotVisible = false
                        binding.progressBar.gone()
                        alertDialogIsNotVisible = false
                        showSimpleDialog()
                    }
                }
            }
        }
    }

    private fun showErrorDialog() {
        val builder = AlertDialog.Builder(this@GameActivity)
        builder.setTitle("Offline")
            .setMessage("We have something wrong, please try again later.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                this@GameActivity.finish()
            }
        builder.create().show()
    }

    private fun setupView(list: List<UserWillowUi>) {
        binding.tvPerson.text = list[0].name

        binding.recycler.layoutManager = GridLayoutManager(this, 2)
        val adapter = GameAdapter(
            list
        ) {
            gameViewModel.checkIfAnswerIsCorrect(it)
        }
        binding.recycler.adapter = adapter
    }

    private fun startTimer(toolbarProgress: ProgressBar) {
        gameViewModel.startedGame()
        Handler().postDelayed(object : Runnable {
            override fun run() {
                if (counterTimer <= MAX_INTERACTIONS) {
                    toolbarProgress.progress = counterTimer
                    counterTimer++
                    Handler().postDelayed(this, 200)
                } else {
                    if (alertDialogIsNotVisible) {
                        showSimpleDialog()
                    }
                    Handler().removeCallbacks(this)
                }
            }
        }, 200)
    }

    fun showSimpleDialog() {
        builder = AlertDialog.Builder(this@GameActivity)
        builder?.setTitle("Game Over")
            ?.setMessage("Your scored ${gameViewModel.getCountCorrectAnswer()}/6")
            ?.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                alertDialogIsNotVisible = true
                this@GameActivity.finish()
            }
        builder?.create()?.show()
    }

    private fun configureToolbar(): ProgressBar {
        setSupportActionBar(binding.toolbar.rootToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolbarTitle = binding.toolbar.toolbarTitle
        val toolbarProgress = binding.toolbar.toolbarProgress

        toolbarTitle.text = getString(R.string.label_practice_mode)

        binding.toolbar.rootToolbar.title = ""
        binding.toolbar.rootToolbar.subtitle = ""
        binding.toolbar.rootToolbar.navigationIcon?.setTint(
            resources.getColor(R.color.white, null)
        )
        return toolbarProgress
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed() // Handle back button
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}