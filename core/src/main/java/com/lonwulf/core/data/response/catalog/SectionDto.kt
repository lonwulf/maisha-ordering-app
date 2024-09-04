@file:Suppress("ConstructorParameterNaming")

package com.lonwulf.core.data.response.catalog

import com.squareup.moshi.Json

/**
 * Class representing api response from query
 *
 * @property data api response
 */
data class SectionDto(
    @Json(name = "homepage_data")
    val homepage_data: HomePageData? = null,

    @Json(name = "latest_sync_date")
    val latest_sync_date: Double = 0.0
) {

    data class HomePageData(
        @Json(name = "sections")
        val sections: List<Sections>,
    ) {
        data class Products(
            @Json(name = "active")
            val active: Boolean? = null,

            @Json(name = "categ_id")
            val categ_id: Int? = null,

            @Json(name = "attributes")
            val attributes: List<Sections.Attribute>? = null,

            @Json(name = "code")
            val code: String? = null,

            @Json(name = "description")
            val description: String? = null,

            @Json(name = "featured")
            val featured: Boolean? = null,

            @Json(name = "id")
            val id: Int? = null,

            @Json(name = "image_date")
            val image_date: Double? = null,

            @Json(name = "images")
            val images: List<Sections.Images>? = null,

            @Json(name = "name")
            val name: String? = null,

            @Json(name = "price")
            val price: Double? = null,

            @Json(name = "priority")
            val priority: Int? = null,

            @Json(name = "product_tmpl_id")
            val product_tmpl_id: Int? = null,

            @Json(name = "sale_ok")
            val sale_ok: Boolean? = true,

            @Json(name = "max_sale_qty")
            val max_sale_qty: Int? = null,

            @Json(name = "min_sale_qty")
            val min_sale_qty: Int? = null,

            @Json(name = "sale_delay")
            val sale_delay: Double,

            @Json(name = "sla_quantity")
            val sla_quantity: Int,

            @Json(name = "sla_days")
            val sla_days: Int
        )

        data class Sections(
            @Json(name = "active")
            val active: Boolean? = false,

            @Json(name = "section_title")
            val section_title: String? = "",

            @Json(name = "view_type")
            val view_type: String? = "",

            @Json(name = "id")
            val id: Int? = 0,

            @Json(name = "section_data")
            val section_data: List<SectionData>?,

            @Json(name = "priority")
            val priority: Int? = 0
        ) {

            data class SectionData(
                @Json(name = "id")
                val id: Int? = 0,

                @Json(name = "active")
                val active: Boolean? = false,

                @Json(name = "title")
                val title: String? = "",

                @Json(name = "image")
                val image: String? = "",

                @Json(name = "video")
                val video: String? = "",

                @Json(name = "product_ids")
                val product_ids: List<Int>?,

                @Json(name = "promos")
                val promos: List<Promos>?
            )

            data class Attribute(
                @Json(name = "name")
                val name: String? = null,

                @Json(name = "type")
                val type: String? = null,

                @Json(name = "value")
                val value: String? = null
            )

            data class Promos(
                @Json(name = "active")
                val active: Boolean? = false,

                @Json(name = "id")
                val id: Int? = 0,

                @Json(name = "product_id")
                val product_id: Int? = 0,

                @Json(name = "savings")
                val savings: Int? = 0,

                @Json(name = "start_date")
                val start_date: String? = "",

                @Json(name = "end_date")
                val end_date: String? = "",

                @Json(name = "priority")
                val priority: Int? = 0
            )

            data class Images(
                @Json(name = "position")
                val position: Int? = null,

                @Json(name = "url")
                val url: String? = null
            )
        }
    }
}
