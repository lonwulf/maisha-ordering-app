@file:Suppress("ConstructorParameterNaming")

package com.lonwulf.core.data.response.partner

import com.squareup.moshi.Json

data class PartnerDetailsDto(
    @Json(name = "data")
    val data: Data? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "status")
    val status: Int? = null,
) {

    data class Data(
        @Json(name = "agent_assigned_to") var agent_assigned_to: String? = null,
        @Json(name = "agent_photo") var agent_photo: String? = null,
        @Json(name = "agent_type") var agent_type: String? = null,
        @Json(name = "class") var agent_class: MutableList<String>? = arrayListOf(),
        @Json(name = "credit_days") var credit_days: String? = null,
        @Json(name = "deactivation_reason") var deactivation_reason: String? = null,
        @Json(name = "id") var id: Long? = null,
        @Json(name = "is_prepay_agent") var is_prepay_agent: Boolean? = false,
        @Json(name = "latitude") var latitude: Double? = null,
        @Json(name = "location") var location: String? = null,
        @Json(name = "longitude") var longitude: Double? = null,
        @Json(name = "name") var name: String? = null,
        @Json(name = "no_copiapesa_redemption") var no_copiapesa_redemption: Boolean? = false,
        @Json(name = "ordering_channels") var ordering_channels: String? = null,
        @Json(name = "partner_type") var partner_type: String? = null,
        @Json(name = "phone") var phone: String? = null,
        @Json(name = "prepayments_exempted") var prepayments_exempted: Boolean? = null,
        @Json(name = "price_list") var price_list: String? = null,
        @Json(name = "price_list_id") var price_list_id: Long? = null,
        @Json(name = "receiving_agent") var receiving_agent: Boolean? = null,
        @Json(name = "warehouse") var warehouse: String? = null,
        @Json(name = "delivery_class") var delivery_class: String? = null,
        @Json(name = "customer_limit") var customer_limit: Double? = null,
        @Json(name = "first_name") var first_name: String? = null,
        @Json(name = "last_name") var last_name: String? = null,
    )
}
