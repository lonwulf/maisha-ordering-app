@file:Suppress("PackageName")

package com.lonwulf.cart.data.response

import com.squareup.moshi.Json

data class AgentMarkersDto(
    @Json(name = "data") val data: List<Data>,
    @Json(name = "message") val message: String? = null,
    @Json(name = "status") val status: Int? = null
) {
    data class Data(
        @Json(name = "Shop_name") val Shop_name: String,
        @Json(name = "display_name") val display_name: String,
        @Json(name = "phone") val phone: String,
        @Json(name = "longitude") val longitude: Double,
        @Json(name = "latitude") val latitude: Double,
        @Json(name = "agent_picture") val agent_picture: String,
        @Json(name = "shop_picture") val shop_picture: String,
        @Json(name = "company_id") val company_id: Int
    )
}
