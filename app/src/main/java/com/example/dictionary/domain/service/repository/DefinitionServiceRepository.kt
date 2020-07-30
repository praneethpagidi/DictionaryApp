package com.example.dictionary.domain.service.repository

import com.example.dictionary.domain.model.ServiceResponse
import com.example.dictionary.domain.service.ServiceResult
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by PraNeeTh on 4/7/20
 */
interface DefinitionServiceRepository  {
    suspend fun getSearchResult(@Header("x-rapidapi-host") host: String,
                                @Header("x-rapidapi-key") key: String,
                                @Query("term") searchWord: String
    ): ServiceResult<ServiceResponse>
}
