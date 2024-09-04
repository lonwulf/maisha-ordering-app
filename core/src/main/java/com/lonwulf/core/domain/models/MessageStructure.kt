package com.lonwulf.core.domain.models

import java.util.Date

data class MessageStructure(
    var title: String,
    var message: String,
    var body: String,
    var senderId: String,
    var productId: Int = 0,
    var promotionId: Int = 0,
    var url: String,
    var isRead: Boolean = false,
    var id: Int,
    var productCodes: List<String> = listOf(),
    var categoryNames: List<String> = listOf(),
    var timeDifference: String = "",
    var notificationProducts: List<Product> = listOf(),
    var dateCreated: Date? = Date(),
    var isLinked: Boolean? = false,
    var sectionIdentifier: String? = null,
    var sectionId: Int? = null,
)
