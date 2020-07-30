package com.example.dictionary.domain.service

/**
 * Created by PraNeeTh on 4/7/20
 */
sealed class ServiceResult <out R> {
    data class Success<out T>(val data: T): ServiceResult<T>()
    data class Error(val exception: ServiceException): ServiceResult<Nothing>()

    data class ServiceException(val code: String?, val string: String)
}