package com.nicco.willowthreedemo

import com.nicco.willowthreedemo.data.model.UserResponse
import com.nicco.willowthreedemo.domain.UserRepository
import com.nicco.willowthreedemo.domain.WillowUseCase
import com.nicco.willowthreedemo.framework.util.ResultWillow
import com.nicco.willowthreedemo.domain.rules.WillowGameRules
import com.nicco.willowthreedemo.presentation.model.UserWillowUi
import com.nicco.willowthreedemo.presentation.viewmodel.GameStateView
import com.nicco.willowthreedemo.presentation.viewmodel.GameViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    /**
     * Github Action, Bitcuket, Bitrise, Jenkins
     *
     * Check code Style -
     * Unit Test -
     * Create a demo version app with no keystore -
     * Create a final app with keystore (keystore is in CI)
     *
     * Firebase, checking Analytics, Crashlytics, Feature Toggle
     *
     * SDK Incode (Check the )
     * SDK counts steps
     *
     * Online and Offline apps datasource Local/Remote, Repository
     *
     * Sqlite / Room / Realm..
     *
     * Server Driver User Interface -> Know your customers ( backend type components )
     *
     * JavaSpring
     */

    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    private lateinit var gameViewModel: GameViewModel

    private val mockWillowUseCase: WillowUseCase = mockk()
    private val mockWillowGameRules: WillowGameRules = mockk()
    val response = UserWillowUi("", "")

    @Before
    fun before() = runTest(mainDispatcherRule.dispatcher) {
        coEvery {
            mockWillowGameRules.getRandomItemsWithoutRepeats(any())
        } returns emptyList()

        coEvery {
            mockWillowUseCase.getUser()
        } returns ResultWillow.Success(emptyList())

        gameViewModel = GameViewModel(mockWillowGameRules, mockWillowUseCase)
    }

    @Test
    fun `should fetch data from view model when create instance of`() =
        runTest(mainDispatcherRule.dispatcher) {
            val state = gameViewModel.userList.value

            coVerify {
                mockWillowUseCase.getUser()
            }

            assertThat(state, IsInstanceOf(GameStateView.SuccessList::class.java))
        }

    @Test
    fun `should check correct answer initial value is 0`() =
        runTest(mainDispatcherRule.dispatcher) {
            val value = gameViewModel.getCountCorrectAnswer()

            assert(value == 0)
        }

    @Test
    fun `should check correct answer initial value is 0 when started game`() =
        runTest(mainDispatcherRule.dispatcher) {
            gameViewModel.startedGame()
            val value = gameViewModel.getCountCorrectAnswer()

            assert(value == 0)
        }

    @Test
    fun `should assert answer and check value of correct answers`() =
        runTest(mainDispatcherRule.dispatcher) {

            /**
             * remember to update countCorrectAnswer to validate
             */
            coEvery {
                mockWillowGameRules.checkIfGameEnded(1, 0)
            } returns false

            coEvery {
                mockWillowGameRules.getRandomItemsWithoutRepeats(eq(emptyList()))
            } returns emptyList()

            coEvery {
                mockWillowGameRules.checkIfAnswerIsCorrect(eq(response), eq(emptyList()))
            } returns true


            gameViewModel.startedGame()
            gameViewModel.checkIfAnswerIsCorrect(response)
            val value = gameViewModel.getCountCorrectAnswer()

            assert(value == 1)
        }

    @Test
    fun `should not assert answer and check value of correct answers`() =
        runTest(mainDispatcherRule.dispatcher) {

            coEvery {
                mockWillowGameRules.getRandomItemsWithoutRepeats(eq(emptyList()))
            } returns emptyList()

            /**
             * remember to update countIncorrectAnswer to validate
             */
            coEvery {
                mockWillowGameRules.checkIfGameEnded(0, 1)
            } returns false


            coEvery {
                mockWillowGameRules.checkIfAnswerIsCorrect(eq(response), eq(emptyList()))
            } returns false


            gameViewModel.startedGame()
            gameViewModel.checkIfAnswerIsCorrect(response)
            val value = gameViewModel.getCountCorrectAnswer()

            assert(value == 0)

            val state = gameViewModel.userList.value
            assertThat(state, IsInstanceOf(GameStateView.SuccessList::class.java))
        }

    @Test
    fun `should not assert answer and check game is ended because empty list`() =
        runTest(mainDispatcherRule.dispatcher) {

            coEvery {
                mockWillowGameRules.getRandomItemsWithoutRepeats(eq(emptyList()))
            } returns emptyList()

            /**
             * remember to update countIncorrectAnswer to validate
             */
            coEvery {
                mockWillowGameRules.checkIfGameEnded(0, 1)
            } returns true


            coEvery {
                mockWillowGameRules.checkIfAnswerIsCorrect(eq(response), eq(emptyList()))
            } returns false


            gameViewModel.startedGame()
            gameViewModel.checkIfAnswerIsCorrect(response)
            val value = gameViewModel.getCountCorrectAnswer()

            assert(value == 0)

            val state = gameViewModel.userList.value
            assertThat(state, IsInstanceOf(GameStateView.ShowDialogGameEnded::class.java))
        }

    @Test
    fun `should check error state`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery {
            mockWillowUseCase.getUser()
        } returns ResultWillow.Failure("Error!")

        gameViewModel = GameViewModel(mockWillowGameRules, mockWillowUseCase)

        val state = gameViewModel.userList.value
        assertThat(state, IsInstanceOf(GameStateView.Error::class.java))
    }

    @Test
    fun `should check error state when response is null`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery {
            mockWillowUseCase.getUser()
        } returns ResultWillow.Success(null)

        gameViewModel = GameViewModel(mockWillowGameRules, mockWillowUseCase)

        val state = gameViewModel.userList.value
        assertThat(state, IsInstanceOf(GameStateView.Error::class.java))
    }
}
