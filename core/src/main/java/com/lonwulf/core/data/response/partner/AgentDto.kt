@file:Suppress("ConstructorParameterNaming")

package com.lonwulf.core.data.response.partner

import com.squareup.moshi.Json

data class AgentDto(
    @Json(name = "data") val data: List<Data>,
    @Json(name = "message") val message: String? = null,
    @Json(name = "status") val status: Int
) {
    data class Data(
        @Json(name = "agent_photo") val agent_photo: String,
        @Json(name = "latitude") val latitude: Double,
        @Json(name = "last_order_date") val last_order_date: String,
        @Json(name = "longitude") val longitude: Double,
        @Json(name = "name") val name: String,
        @Json(name = "shop_location") val shop_location: String,
        @Json(name = "id") val id: Int,
        @Json(name = "phone") val phone: String,
        @Json(name = "premise_image") val premise_image: String,
        @Json(name = "shop_name") val shop_name: String,
        @Json(name = "warehouse_name") val warehouse_name: String?,
        @Json(name = "delivery_class") val delivery_class: String?
    )
}
