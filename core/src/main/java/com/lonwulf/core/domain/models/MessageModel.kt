package com.lonwulf.core.domain.models

import com.squareup.moshi.Json
import java.util.Date

data class MessageModel(
    @Json(name = "title")
    var title: String? = null,

    @Json(name = "message")
    var message: String? = null,

    @Json(name = "body")
    var body: String? = null,

    @Json(name = "product_codes")
    var product_codes: List<String>? = null,

    @Json(name = "category_names")
    var category_names: List<String>? = null,

    @Json(name = "product_id")
    var productId: Int = 0,

    @Json(name = "promotion_id")
    var promotionId: Int = 0,

    @Json(name = "url")
    var url: String? = null,

    @Json(name = "company_id")
    var company_id: Int? = null,

    @Json(name = "sender_id")
    var sender_id: String? = null,

    var isRead: Boolean? = false,
    var id: Int = 0,
    var dateCreated: Date = Date(),
    var isLinked: Boolean? = false,
    var sectionIdentifier: String? = null,
    var sectionId: Int? = null
) : Comparable<MessageModel> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true // if both of them points the same address in memory
        if (other !is MessageModel) return false // if "that" is not a People or a childclass
        val messageStructure: MessageModel = other // than we can cast it to People safely
        return id == messageStructure.id // if they have the same name and same age, then the 2 objects are equal unless they're pointing to different memory adresses
    }

    override operator fun compareTo(other: MessageModel): Int {
        return when {
            dateCreated > other.dateCreated -> {
                -1
            }

            dateCreated < other.dateCreated -> {
                1
            }

            else -> {
                0
            }
        }
    }

    override fun hashCode(): Int {
        var result = title?.hashCode() ?: 0
        result = 31 * result + (message?.hashCode() ?: 0)
        result = 31 * result + (body?.hashCode() ?: 0)
        result = 31 * result + productId
        result = 31 * result + promotionId
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (isRead.hashCode())
        result = 31 * result + (id.hashCode())
        result = 31 * result + (dateCreated.hashCode())
        return result
    }
}
