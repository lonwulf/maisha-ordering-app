package com.lonwulf.core.utils

/**
 * wrapper for successful insertion, retrieval e.t.c. of data to cache
 */
sealed class CacheResult<out T> {
    data class Success<out T>(val value: T) :
        CacheResult<T>()
    data class GenericError(val errormsg: String? = null) :
        CacheResult<Nothing>()
}
