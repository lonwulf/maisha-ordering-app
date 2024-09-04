package com.lonwulf.core.data.response

import com.squareup.moshi.Json

data class AgentSearchDto(
    @Json(name = "data") val data: List<Data>,
    @Json(name = "message") val message: String? = null,
    @Json(name = "status") val status: Int? = null
) {
    data class Data(
        @Json(name = "agent_image") val agent_image: String,
        @Json(name = "latitude") val latitude: Double,
        @Json(name = "location") val location: String,
        @Json(name = "longitude") val longitude: Double,
        @Json(name = "name") val name: String,
        @Json(name = "nearest_landmark") val nearest_landmark: String,
        @Json(name = "partner_id") val partner_id: Int,
        @Json(name = "phone") val phone: String,
        @Json(name = "premise_image") val premise_image: String,
        @Json(name = "shop_name") val shop_name: String,
        @Json(name = "warehouse") val warehouse: String?,
        @Json(name = "delivery_class") val delivery_class: String?
    )
}
