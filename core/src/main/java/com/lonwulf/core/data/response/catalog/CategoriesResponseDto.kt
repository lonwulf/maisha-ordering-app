package com.lonwulf.core.data.response.catalog

import com.squareup.moshi.Json

/**
 * Class representing Category api response from query
 *
 * @property data api response
 */
data class CategoriesResponseDto(
    @Json(name = "data")
    val data: Data? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "status")
    val status: Int? = null
) {

    data class Data(
        @Json(name = "categories")
        val categories: List<Categories>? = null
    ) {

        data class Categories(
            @Json(name = "display_name")
            val display_name: String? = null,

            @Json(name = "featured")
            val featured: Boolean? = null,

            @Json(name = "id")
            val id: Int? = null,

            @Json(name = "images")
            val images: List<Images>? = null,

            @Json(name = "name")
            val name: String? = null,

            @Json(name = "parent_id")
            val parent_id: Int? = null,

            @Json(name = "priority")
            val priority: Int? = null
        ) {

            data class Images(
                @Json(name = "position")
                val position: Int? = null,

                @Json(name = "url")
                val url: String? = null
            )
        }
    }
}
