package com.lonwulf.core.data.request

data class PromoRequestBody(
    val app_id: Int,
    val last_sync_date: Double,
    val channel: String,
    val partner_id: Int? = null,
)
