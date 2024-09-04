package com.lonwulf.core.state

sealed class GenericApiResultState {
    object Loading : GenericApiResultState()
    data class Success(val isSuccessful: Boolean, val data: Any? = null) : GenericApiResultState()
    object Empty : GenericApiResultState()
    data class Error(val errorMsg: String) : GenericApiResultState()
}

/**
 * Ideally generic being driven by type of [T]
 *
 * @param T object
 */
sealed class ApiResultState<out T> {
    data class Loading(val loading: Boolean) : ApiResultState<Boolean>()
    data class Success<T>(val item: T) : ApiResultState<T>()
    data class Error(val error: Throwable) : ApiResultState<Nothing>()
}
