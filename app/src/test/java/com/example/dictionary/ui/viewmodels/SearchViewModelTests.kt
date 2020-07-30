package com.example.dictionary.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dictionary.domain.database.repository.DefinitionDatabaseRepository
import com.example.dictionary.domain.model.DefinitionModel
import com.example.dictionary.domain.model.ServiceResponse
import com.example.dictionary.domain.service.ServiceResult
import com.example.dictionary.domain.service.repository.DefinitionServiceRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.koin.core.context.stopKoin
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString

/**
 * Created by PraNeeTh on 4/13/20
 */
@ExperimentalCoroutinesApi
class SearchViewModelTests {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var searchViewModel: SearchViewModel

    @MockK
    lateinit var serviceRepository: DefinitionServiceRepository

    @MockK
    lateinit var databaseRepository: DefinitionDatabaseRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        searchViewModel = spyk(SearchViewModel(serviceRepository, databaseRepository))
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    fun cleanUp() {
//        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `when searchForWord is invoked and service is not success should invoke handleErrorAndShowToast`() {
        val errorString = "some error"
        coEvery {
            serviceRepository.getSearchResult(
                any(),
                any(),
                any()
            )
        } returns ServiceResult.Error(
            ServiceResult.ServiceException(null, errorString)
        )
        coEvery { databaseRepository.getDefinitions(any()) } returns anyList()
        every { searchViewModel.handleErrorAndShowToast(anyList()) } just runs

        searchViewModel.searchForWord(anyString())

        coVerify { serviceRepository.getSearchResult(any(), any(), any()) }
        coVerify { databaseRepository.getDefinitions(any()) }
        verify { searchViewModel.handleErrorAndShowToast(anyList()) }
    }


    @Ignore("Need some research on why a mocked suspend function is being run and throwing an" +
            " exception https://github.com/mockk/mockk/issues/288")
    @Test
    fun `when searchForWord is invoked and service is success should invoke handleSuccessAndOperateDatabase`() {
        val mockResponse: ServiceResponse = mockk()
        val mockDefinitionsList: List<DefinitionModel> = mockk()
        coEvery {
            serviceRepository.getSearchResult(
                any(),
                any(),
                any()
            )
        } returns ServiceResult.Success(
            mockResponse
        )
        coEvery { databaseRepository.getDefinitions(any()) } returns anyList()
        coEvery {
            searchViewModel.handleSuccessAndOperateDatabase(
                mockResponse,
                mockDefinitionsList
            )
        } just runs

        searchViewModel.searchForWord(anyString())

        coVerify { serviceRepository.getSearchResult(any(), any(), any()) }
        coVerify { databaseRepository.getDefinitions(any()) }
        coVerify {
            searchViewModel.handleSuccessAndOperateDatabase(
                mockResponse,
                mockDefinitionsList
            )
        }
    }
}