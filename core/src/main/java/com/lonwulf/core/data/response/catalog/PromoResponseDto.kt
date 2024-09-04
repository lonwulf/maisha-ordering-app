@file:Suppress("PackageNaming", "ConstructorParameterNaming")

package com.lonwulf.core.data.response.catalog

import com.squareup.moshi.Json

data class PromoResponseDto(
    @Json(name = "data")
    val data: Data? = null,
    @Json(name = "message")
    val message: String? = null,
    @Json(name = "status")
    val status: Int? = null
) {
    data class Data(
        @Json(name = "latest_sync_date")
        val latest_sync_date: Double? = null,
        @Json(name = "product_discounts")
        val product_discounts: Promo? = null,
        @Json(name = "category_discounts")
        val category_discounts: Promo? = null,
        @Json(name = "promos") // old api
        val promos: List<PromoData>? = null
    )

    data class Promo(
        @Json(name = "active")
        val active: Boolean? = false,
        @Json(name = "data")
        val data: List<PromoData>? = emptyList()
    )

    data class PromoData(
        @Json(name = "id")
        val id: Int? = null,
        @Json(name = "product_category_id")
        val product_category_id: Int? = null,
        @Json(name = "product_id")
        val product_id: Long? = null,
        @Json(name = "hide_promo_price")
        val hide_promo_price: Boolean? = false,
        @Json(name = "type")
        val type: String? = null,
        @Json(name = "channel_applicable")
        val channel_applicable: List<String>? = emptyList(),
        @Json(name = "value")
        val value: Double? = null,
        @Json(name = "end_date")
        val end_date: String? = null,
        @Json(name = "start_date")
        val start_date: String? = null,
        @Json(name = "savings") // old api
        val savings: Double? = null,
        @Json(name = "active")
        val active: Boolean? = false,
        @Json(name = "max_order_qty")
        val max_order_qty: Int? = null
    )
}
