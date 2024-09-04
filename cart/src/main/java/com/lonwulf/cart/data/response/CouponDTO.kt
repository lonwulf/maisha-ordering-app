@file:Suppress("PackageNaming")

package com.lonwulf.cart.data.response

import com.squareup.moshi.Json

data class CouponDTO(
    @Json(name = "data") var data: Data? = null,

    @Json(name = "message") var message: String? = "",

    @Json(name = "status") var status: Int? = null
) {
    @Suppress("ConstructorParameterNaming")
    data class Data(
        @Json(name = "is_valid") var is_valid: Boolean? = false,
        @Json(name = "minimum_amount") var minimum_amount: Double? = 0.0,
        @Json(name = "message") var message: String? = "",

        @Json(name = "discount") var discount: Double? = 0.0,
        @Json(name = "product_ids") var product_ids: List<Int>? = emptyList(),
        @Json(name = "category_ids") var category_ids: List<Int>? = emptyList(),

        @Json(name = "coupon_id") var coupon_id: Int? = 0
    )
}
