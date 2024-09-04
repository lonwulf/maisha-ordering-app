@file:Suppress("PackageName", "ConstructorParameterNaming")

package com.lonwulf.cart.data.response

import com.squareup.moshi.Json

data class NearestAgentDTO(
    @Json(name = "status") var status: Int? = null,
    @Json(name = "message") var message: String? = "",
    @Json(name = "data") var data: List<Details>? = ArrayList(),
) {
    data class Details(
        @Json(name = "geoJson") var geoJson: String? = "",
        @Json(name = "is_remote_agent") var is_remote_agent: Boolean? = false,
        @Json(name = "name") var name: String? = "",
        @Json(name = "display_name") var display_name: String? = "",
        @Json(name = "partner_id") var partner_id: Int? = null
    )
}
