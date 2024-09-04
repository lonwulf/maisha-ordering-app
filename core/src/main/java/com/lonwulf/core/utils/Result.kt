package com.lonwulf.core.utils

import com.lonwulf.core.network.HttpResult

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val cause: HttpResult,
        val code: Int? = null,
        val errorMessage: String? = null
    ) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[cause=$cause, code=$code, message=$errorMessage]"
            is Loading -> "Loading"
        }
    }
}
