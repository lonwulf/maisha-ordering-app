package com.lonwulf.presentation.state

sealed class GenericResultState<out T> {
    object Loading : GenericResultState<Nothing>()
    object Empty : GenericResultState<Nothing>()
    data class Success<T>(val results: T? = null) : GenericResultState<T>()
    data class Error(val msg: String? = "") : GenericResultState<Nothing>()
}