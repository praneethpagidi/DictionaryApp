package com.example.dictionary.domain.service

import android.util.Log
import retrofit2.Response

/**
* Created by PraNeeTh on 4/7/20
*/
object RetrofitCallBackHandler {
    suspend fun <T> processServiceCall(call: suspend () -> Response<T>): ServiceResult<T> {
        return try {
            val serviceCallResponse = call()
            val body = serviceCallResponse.body()
            if (serviceCallResponse.isSuccessful && body != null) {
                ServiceResult.Success(body)
            } else {
                logExceptionMessage(
                    serviceCallResponse.message().orEmpty(),
                    serviceCallResponse.code().toString()
                )
                generateServiceError(
                    "${serviceCallResponse.code()}"
                )
            }
        } catch (exception: Exception) {
            logExceptionMessage(
                exception.localizedMessage ?: "",
                null
            )
            generateServiceError(
                null
            )
        }
    }

    private fun generateServiceError(
        errorCode: String?
    ): ServiceResult.Error {
        return ServiceResult.Error(
            ServiceResult.ServiceException(
                errorCode,
                "Sorry, there was a service issue, please try again"
            )
        )
    }

    fun logExceptionMessage(message: String, code: String?) {
        val codeMessage = if (code == null) "" else "with code: $code; "
        Log.d("exception", "$codeMessage$message")
    }
}