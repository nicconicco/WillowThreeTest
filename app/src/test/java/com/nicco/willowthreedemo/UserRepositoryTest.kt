package com.nicco.willowthreedemo

import com.nicco.willowthreedemo.data.repository.UserRepositoryImpl
import com.nicco.willowthreedemo.domain.UserRepository
import com.nicco.willowthreedemo.framework.ApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class UserRepositoryTest {

    private val mockContentType: MediaType = mockk()
    private val mockBody: ResponseBody = mockk()
    private val mockApiService: ApiService = mockk()
    private lateinit var userRepository: UserRepository

    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    @Before
    fun before() {
        userRepository = UserRepositoryImpl(mockApiService)
    }

    @Test
    fun `should check getUsers was called`() =
        runTest(mainDispatcherRule.dispatcher) {

            coEvery {
                mockApiService.getUsers()
            } returns Response.success(null)

            userRepository.getUser()

            coVerify {
                mockApiService.getUsers()
            }
        }

    @Test
    fun `should check getUsers was called with errors`() =
        runTest(mainDispatcherRule.dispatcher) {

            coEvery {
                mockBody.contentLength()
            } returns 2

            coEvery {
                mockBody.contentType()
            } returns mockContentType

            coEvery {
                mockApiService.getUsers()
            } returns Response.error(404, mockBody)

            userRepository.getUser()

            coVerify {
                mockApiService.getUsers()
            }
        }
}