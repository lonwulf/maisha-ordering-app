package com.lonwulf.core.network

import com.squareup.moshi.Json

@Suppress("UNCHECKED_CAST")
class ApiResponse<T> {

    @Json(name = "data")
    var data = Any() as T

    @Json(name = "message")
    var message: String? = ""

    @Json(name = "status")
    var status: Int = 0
}
