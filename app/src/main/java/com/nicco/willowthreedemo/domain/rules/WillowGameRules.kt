package com.nicco.willowthreedemo.domain.rules

import com.nicco.willowthreedemo.presentation.model.UserWillowUi

interface WillowGameRules {
    fun getRandomItemsWithoutRepeats(list: List<UserWillowUi>): List<UserWillowUi>
    fun checkIfAnswerIsCorrect(userUi: UserWillowUi, listUserUi: List<UserWillowUi>): Boolean
    fun checkIfGameEnded(
        countCorrectAnswer: Int,
        countIncorrectAnswer: Int
    ): Boolean
}