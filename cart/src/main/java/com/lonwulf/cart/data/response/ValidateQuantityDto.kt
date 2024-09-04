@file:Suppress("ConstructorParameterNaming", "PackageNaming")

package com.lonwulf.cart.data.response

import com.squareup.moshi.Json

data class ValidateQuantityDto(
    @Json(name = "data")
    val data: List<Data>
) {
    data class Data(
        @Json(name = "product_id")
        val product_id: String,
        @Json(name = "stock")
        val stock: Stock?
    ) {
        data class Stock(
            @Json(name = "default_code")
            val default_code: String,
            @Json(name = "qoh")
            val qoh: Double,
            @Json(name = "reject_orders")
            val reject_orders: Boolean,
            @Json(name = "track_stock")
            val track_stock: Boolean
        )
    }
}
