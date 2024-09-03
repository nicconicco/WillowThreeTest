package com.nicco.willowthreedemo

import com.nicco.willowthreedemo.domain.rules.WillowGameRules
import com.nicco.willowthreedemo.domain.rules.WillowGameRulesImpl
import com.nicco.willowthreedemo.presentation.model.UserWillowUi
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WillowGameRulesTest {

    private lateinit var willowGameRules: WillowGameRules

    @Before
    fun before() {
        willowGameRules = WillowGameRulesImpl()
    }

    @Test
    fun `checkIfAnswerIsCorrect should be true`() {
        val response = UserWillowUi("Carlos", "urlimage")

        val list = listOf(
            response
        )

        val result = willowGameRules.checkIfAnswerIsCorrect(response, list)

        assertTrue(result)
    }

    @Test
    fun `checkIfAnswerIsCorrect should be false`() {
        val response = UserWillowUi("Carlos", "urlimage")
        val response2 = UserWillowUi("Carlos", "urlimage2")

        val list = listOf(
            response2
        )

        val result = willowGameRules.checkIfAnswerIsCorrect(response, list)

        assertFalse(result)
    }

    @Test
    fun `checkIfAnswerIsCorrect should be false when empty list`() {
        val response = UserWillowUi("Carlos", "urlimage")

        val list = listOf<UserWillowUi>()

        val result = willowGameRules.checkIfAnswerIsCorrect(response, list)

        assertFalse(result)
    }

    @Test
    fun `getRandomItemsWithoutRepeats should bring item`() {
        val response = UserWillowUi("Carlos", "urlimage")
        val response2 = UserWillowUi("Carlos 2", "urlimage2")
        val list = listOf(response, response2)

        val result = willowGameRules.getRandomItemsWithoutRepeats(list)

        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `checkIfGameEnded should be true`() {
        val result = willowGameRules.checkIfGameEnded(3, 3)

        assertTrue(result)
    }

    @Test
    fun `checkIfGameEnded should be false`() {
        val result = willowGameRules.checkIfGameEnded(2, 3)

        assertFalse(result)
    }

    @Test(expected = Exception::class)
    fun `checkIfGameEnded should throw exception`() {
        willowGameRules.checkIfGameEnded(5, 3)
    }
}

