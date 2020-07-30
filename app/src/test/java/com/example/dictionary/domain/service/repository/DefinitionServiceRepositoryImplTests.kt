package com.example.dictionary.domain.service.repository

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dictionary.domain.model.ServiceResponse
import com.example.dictionary.domain.service.DefinitionService
import com.example.dictionary.domain.service.ServiceResult
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import retrofit2.Response
import org.hamcrest.CoreMatchers.`is` as match

/**
 * Created by PraNeeTh on 4/13/20
 */
@ExperimentalCoroutinesApi
class DefinitionServiceRepositoryImplTests {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var definitionService: DefinitionService

    private lateinit var definitionServiceRepositoryImpl: DefinitionServiceRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        definitionServiceRepositoryImpl =
            spyk(DefinitionServiceRepositoryImpl(definitionService, Dispatchers.Unconfined))
    }

    @Test
    fun `getSearchResult when invoked if there is an error should return the result itself`() {
        runBlockingTest {
            val mockResponse: Response<ServiceResponse> = mockk()
            every { mockResponse.isSuccessful } returns false
            every { mockResponse.body() } returns null
            every { mockResponse.code() } returns 123
            every { mockResponse.message() } returns "BAD RESPONSE"
            mockkStatic(Log::class)
            every { Log.d(any(), any()) } returns 0
            coEvery { definitionService.getSearchResult(any(), any(), any()) } returns mockResponse

            val result = definitionServiceRepositoryImpl.getSearchResult(
                anyString(),
                anyString(),
                anyString()
            )

            coVerify { definitionService.getSearchResult(any(), any(), any()) }

            val resultError = result as ServiceResult.Error
            val expectedErrorString = "Sorry, there was a service issue, please try again"
            val expectedServiceException =
                ServiceResult.ServiceException("123", expectedErrorString)
            MatcherAssert.assertThat(resultError.exception, match(expectedServiceException))
        }
    }

    @Test
    fun `getSearchResult when invoked if there is no error should return the result success`() {
        runBlockingTest {
            val mockResponse: Response<ServiceResponse> = mockk()
            val playersResponse: ServiceResponse = mockk()
            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns playersResponse
            coEvery { definitionService.getSearchResult(any(), any(), any()) } returns mockResponse

            val result = definitionServiceRepositoryImpl.getSearchResult(
                anyString(),
                anyString(),
                anyString()
            )

            coVerify { definitionService.getSearchResult(any(), any(), any()) }

            val resultSuccess = result as ServiceResult.Success
            MatcherAssert.assertThat(resultSuccess.data, match(playersResponse))
        }
    }
}