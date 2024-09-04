@file:Suppress("ConstructorParameterNaming")

package com.lonwulf.core.data.response.catalog

import com.squareup.moshi.Json

data class ProductQuantityResponseDto(
    @Json(name = "data")
    val data: Data
) {

    data class Data(
        @Json(name = "count")
        val count: Int,
        @Json(name = "products")
        val products: List<Product>,
        @Json(name = "success")
        val success: Boolean,
        @Json(name = "updatedTime")
        val updatedTime: String
    ) {

        data class Product(
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
}
