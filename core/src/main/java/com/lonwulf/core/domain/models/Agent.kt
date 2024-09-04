package com.lonwulf.core.domain.models

data class Agent(
    val agentImage: String? = "",
    val latitude: Double,
    val location: String,
    val longitude: Double,
    val name: String,
    val nearestLandmark: String? = "",
    val partnerId: Int,
    val phone: String,
    val premiseImage: String? = "",
    val shopName: String? = "",
    val warehouse: String? = "",
    val deliveryClass: String? = ""
)
