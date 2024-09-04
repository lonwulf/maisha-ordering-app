@file:Suppress("PackageNaming", "ConstructorParameterNaming")

package com.lonwulf.core.data.response.catalog

import com.squareup.moshi.Json

data class DeactivatedProductsDto(
    @Json(name = "data")
    val data: Data? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "status")
    val status: Int? = null,
) {
    data class Data(
        @Json(name = "latest_sync_date")
        val latest_sync_date: Double? = null,
        @Json(name = "products")
        val products: List<Int>? = emptyList(),
    )
}
