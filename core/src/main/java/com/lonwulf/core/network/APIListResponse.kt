@file:Suppress("Filename", "AnnotationOnSeparateLine")

package com.lonwulf.core.network

import com.squareup.moshi.Json

class ApiListResponse<T> {
    @Json(name = "data")
    var data: List<T> = ArrayList()

    @Json(name = "message")
    var message: String? = ""

    @Json(name = "status")
    var status: Int = 0
}
