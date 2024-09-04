package com.lonwulf.core.data.request

data class DeactivatedProductsRequestBody(
    val app_id: Int,
    val last_sync_date: Double,
    val company_id: Int
)
