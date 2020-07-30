package com.example.dictionary.domain.service

import com.example.dictionary.domain.model.ServiceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by PraNeeTh on 4/7/20
 */
interface DefinitionService {
    @GET("define")
    suspend fun getSearchResult(@Header("x-rapidapi-host") host: String,
                                @Header("x-rapidapi-key") key: String,
                                @Query("term") word: String
    ): Response<ServiceResponse>
}