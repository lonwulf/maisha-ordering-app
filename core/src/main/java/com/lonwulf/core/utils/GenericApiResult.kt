package com.lonwulf.core.utils

data class GenericApiResult<out T>(
    val results: T,
    val success: Boolean,
    val message: String
)
