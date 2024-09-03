package com.nicco.willowthreedemo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.nicco.willowthreedemo.domain.WillowUseCase
import com.nicco.willowthreedemo.domain.rules.WillowGameRules
import com.nicco.willowthreedemo.framework.util.flow
import com.nicco.willowthreedemo.presentation.model.UserWillowUi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val willowUtils: WillowGameRules,
    private val willowUseCase: WillowUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private companion object {
        const val START_COUNTER = 0
    }

    private var countIncorrectAnswer: Int = START_COUNTER
    private var countCorrectAnswer: Int = START_COUNTER

    private var listWithAllUser: List<UserWillowUi> = mutableListOf()
    private var randomSelection: List<UserWillowUi> = mutableListOf()

    private val _userList = MutableStateFlow<GameStateView>(GameStateView.Idle)
    val userList: StateFlow<GameStateView> = _userList.asStateFlow()

    private val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler {
            _ , _ ->

            _userList.value = GameStateView.Error
        }

    private val scope: CoroutineScope = CoroutineScope(
        dispatcher + exceptionHandler
    )

    init {
        scope.launch {
            willowUseCase.getUser().flow(
                { list ->
                    list?.let {
                        listWithAllUser = it

                        randomSelection = willowUtils.getRandomItemsWithoutRepeats(it)

                        _userList.value = GameStateView.SuccessList(randomSelection)
                    } ?: run {
                        _userList.value = GameStateView.Error
                    }
                }, {
                    _userList.value = GameStateView.Error
                }
            )
        }
    }

    fun checkIfAnswerIsCorrect(it: UserWillowUi) {
        if (willowUtils.checkIfAnswerIsCorrect(it, randomSelection)) {
            countCorrectAnswer++
            checkIfGameEnded()
        } else {
            countIncorrectAnswer++
            checkIfGameEnded()
        }
    }

    private fun reloadNewListAfterCheckAnswer() {
        randomSelection = willowUtils.getRandomItemsWithoutRepeats(listWithAllUser)
        _userList.value = GameStateView.SuccessList(randomSelection)
    }

    private fun checkIfGameEnded() {
        if (
            willowUtils.checkIfGameEnded(countCorrectAnswer, countIncorrectAnswer)
        ) {
            _userList.value =
                GameStateView.ShowDialogGameEnded(countCorrectAnswer)
        } else {
            reloadNewListAfterCheckAnswer()
        }
    }

    fun startedGame() {
        countCorrectAnswer = START_COUNTER
        countIncorrectAnswer = START_COUNTER
    }

    fun getCountCorrectAnswer(): Int {
        return countCorrectAnswer
    }
}