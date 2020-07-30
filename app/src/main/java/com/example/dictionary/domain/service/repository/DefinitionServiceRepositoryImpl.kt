package com.example.dictionary.domain.service.repository

import com.example.dictionary.domain.model.ServiceResponse
import com.example.dictionary.domain.service.DefinitionService
import com.example.dictionary.domain.service.RetrofitCallBackHandler
import com.example.dictionary.domain.service.ServiceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by PraNeeTh on 4/7/20
 */
class DefinitionServiceRepositoryImpl(
    private val definitionService: DefinitionService,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DefinitionServiceRepository {
    override suspend fun getSearchResult(
        host: String, key: String, searchWord: String
    ): ServiceResult<ServiceResponse> {
        val result = withContext(coroutineDispatcher) {
            RetrofitCallBackHandler.processServiceCall {
                definitionService.getSearchResult(host, key, searchWord)
            }
        }

        return when(result) {
            is ServiceResult.Success -> {
                ServiceResult.Success(result.data)
            }

            is ServiceResult.Error -> {
                result
            }
        }
    }
}