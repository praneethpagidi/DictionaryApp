package com.example.dictionary.util

/**
 * Created by PraNeeTh on 4/8/20
 */
open class SingleLiveDataEvent<out T>(private val content: T) {

    var isDataHandled = false
        private set

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (isDataHandled) {
            null
        } else {
            isDataHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun getContentEvenIfHandled(): T = content
}