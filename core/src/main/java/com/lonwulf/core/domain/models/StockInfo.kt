package com.lonwulf.core.domain.models

data class StockInfo(
    val productId: Int,
    val qoh: Int,
    val rejectOrders: Boolean,
    val trackStock: Boolean
)
