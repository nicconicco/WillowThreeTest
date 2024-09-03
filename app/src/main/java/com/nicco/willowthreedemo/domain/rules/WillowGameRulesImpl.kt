package com.nicco.willowthreedemo.domain.rules

import com.nicco.willowthreedemo.presentation.model.UserWillowUi


class WillowGameRulesImpl : WillowGameRules {

    private companion object {
        const val MAX_NUMBER_PLAYS = 6
    }

    override fun getRandomItemsWithoutRepeats(
        list: List<UserWillowUi>
    ): List<UserWillowUi> {
        if (list.size < MAX_NUMBER_PLAYS) {
            return list.shuffled()
        }

        val shuffledList = list.shuffled()
        return shuffledList.subList(0, MAX_NUMBER_PLAYS)
    }

    override fun checkIfAnswerIsCorrect(it: UserWillowUi, userResponse: List<UserWillowUi>): Boolean {
        if(userResponse.isEmpty()) {
            return false
        }

        return userResponse[0] == it
    }


    override fun checkIfGameEnded(
        countCorrectAnswer: Int,
        countIncorrectAnswer: Int
    ): Boolean {

        if(countCorrectAnswer + countIncorrectAnswer > MAX_NUMBER_PLAYS)
            throw Exception("something was wrong here.")

        return countCorrectAnswer == MAX_NUMBER_PLAYS ||
                countIncorrectAnswer == MAX_NUMBER_PLAYS ||
                countCorrectAnswer + countIncorrectAnswer == MAX_NUMBER_PLAYS
    }
}