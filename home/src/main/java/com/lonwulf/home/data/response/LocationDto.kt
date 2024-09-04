@file:Suppress("PackageNaming", "ConstructorParameterNaming")

package com.lonwulf.home.data.response

import com.squareup.moshi.Json

data class LocationDto(
    @Json(name = "data")
    val data: Data? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "status")
    val status: Int? = null
) {

    data class Data(
        @Json(name = "partner_id")
        val partner_id: Int? = null,

        @Json(name = "latitude")
        val latitude: Double? = null,

        @Json(name = "longitude")
        val longitude: Double? = null

    )
}
