@file:Suppress("PackageNaming")

package com.lonwulf.cart.data.response

import com.squareup.moshi.Json

/**
 * Class representing Category api response from query
 *
 * @property data api response
 */
data class ProcessOrderDto(
    @Json(name = "data")
    val data: Status? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "status")
    val status: Int? = null
) {
    data class Status(
        @Json(name = "status")
        val status: String?
    )
}
