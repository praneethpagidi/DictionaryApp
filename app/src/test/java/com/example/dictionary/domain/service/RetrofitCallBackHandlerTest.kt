package com.example.dictionary.domain.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.io.IOException

/**
 * Created by PraNeeTh on 4/9/20
 */
@ExperimentalCoroutinesApi
class RetrofitCallbackHandlerTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private val testMessage = "TEST MESSAGE"

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun `processServiceCall process exception message and return service error when the call returns exception`() =
        runBlockingTest {
            @Suppress("RedundantSuspendModifier")
            suspend fun testFunction(): Response<String> {
                throw IOException(testMessage)
            }

            val spy = spyk(RetrofitCallBackHandler)
            val answer = spy.processServiceCall(call = ::testFunction)
            verify { spy.logExceptionMessage(testMessage, null) }
            verifyErrorResult(null, answer)
        }

    @Test
    fun `processServiceCall process response error message and return service error when the call returns response with unsuccessful error code`() =
        runBlockingTest {
            val errorCode = 100
            val mockResponse = mockk<Response<String>>()
            every { mockResponse.code() } returns errorCode
            every { mockResponse.message() } returns testMessage
            every { mockResponse.isSuccessful } returns false
            every { mockResponse.body() } returns ""

            suspend fun testFunction(): Response<String> = withContext(Dispatchers.Main) {
                mockResponse
            }

            val spy = spyk(RetrofitCallBackHandler)
            val answer = spy.processServiceCall(call = ::testFunction)
            verify { spy.logExceptionMessage(testMessage, "$errorCode") }
            verifyErrorResult("$errorCode", answer)
        }

    @Test
    fun `processServiceCall process response code and return service error when the call returns response with successful code but without response body`() =
        runBlockingTest {
            val errorCode = 100
            val mockResponse = mockk<Response<String>>()
            every { mockResponse.code() } returns errorCode
            every { mockResponse.message() } returns testMessage
            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns null

            suspend fun testFunction(): Response<String> = withContext(Dispatchers.Main) {
                mockResponse
            }

            val spy = spyk(RetrofitCallBackHandler)
            val answer = spy.processServiceCall(call = ::testFunction)
            verify { spy.logExceptionMessage(testMessage, "$errorCode") }
            verifyErrorResult("$errorCode", answer)
        }

    @Test
    fun `processServiceCall return service success with response body when the call returns response with successful code and body`() =
        runBlockingTest {
            val errorCode = 100
            val mockResponse = mockk<Response<String>>()
            every { mockResponse.code() } returns errorCode
            every { mockResponse.message() } returns testMessage
            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns "SUCCESS"

            suspend fun testFunction(): Response<String> = withContext(Dispatchers.Main) {
                mockResponse
            }

            val spy = spyk(RetrofitCallBackHandler)
            val answer = spy.processServiceCall(call = ::testFunction)
            verify(exactly = 0) { spy.logExceptionMessage(any(), any()) }
            val success = answer as ServiceResult.Success
            assertEquals(success.data, "SUCCESS")
        }

    private fun verifyErrorResult(errorCode: String?, answer: ServiceResult<String>) {
        val sampleError = "Sorry, there was a service issue, please try again"
        val serviceException = ServiceResult.ServiceException(errorCode, sampleError)
        val error = answer as ServiceResult.Error
        assertEquals(error.exception, serviceException)
    }
}