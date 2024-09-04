@file:Suppress("PackageNaming")

package com.lonwulf.cart.domain.model

@Suppress("ConstructorParameterNaming")
data class CouponModel(
    var is_valid: Boolean? = false,
    var minimum_amount: Double? = 0.0,
    var message: String? = "",
    var discount: Double? = 0.0,
    var coupon_id: Int? = 0,
    var productIds: List<Int>? = emptyList(),
    var categoryIds: List<Int>? = emptyList()
)

data class CouponProductDataModel(
    var id: Int? = 0,
    var quantity: Int? = 0
)
