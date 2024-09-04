@file:Suppress("ConstructorParameterNaming")

package com.lonwulf.core.data.response.catalog

import com.lonwulf.core.database.entity.Attributes
import com.squareup.moshi.Json

/**
 * Class representing Product api response from query
 *
 * @property data api response
 */
data class ProductsResponseDto(
    @Json(name = "data")
    val data: Data? = null
) {
    data class Data(
        @Json(name = "success") val success: Boolean? = false,

        @Json(name = "count") val count: Int? = 0,

        @Json(name = "updatedTime") val updatedTime: String? = "",

        @Json(name = "products") val products: List<Products>? = emptyList()
    ) {
        data class Products(
            @Json(name = "active")
            val active: Boolean? = false,

            @Json(name = "categ_id")
            val categ_id: Int? = null,

            @Json(name = "code")
            val code: String? = "",

            @Json(name = "description")
            val description: String? = "",

            @Json(name = "featured")
            val featured: Boolean? = false,

            @Json(name = "id")
            val id: Int? = null,

            @Json(name = "image_date")
            val image_date: Double? = null,

            @Json(name = "images")
            val images: List<Images>? = emptyList(),

            @Json(name = "name")
            val name: String? = "",

            @Json(name = "price")
            val price: Double? = null,

            @Json(name = "pricelist_price")
            val pricelist_price: Double? = null,

            @Json(name = "priority")
            val priority: Int? = null,

            @Json(name = "product_tmpl_id")
            val product_tmpl_id: Int? = null,

            @Json(name = "attributes")
            val attributes: List<Attributes>? = null,

            @Json(name = "sale_ok")
            val sale_ok: Boolean? = null,

            @Json(name = "max_sale_qty")
            val max_sale_qty: Int? = null,

            @Json(name = "min_sale_qty")
            val min_sale_qty: Int? = null,

            @Json(name = "sale_delay")
            val sale_delay: Double,

            @Json(name = "sla_days")
            val sla_days: Int,

            @Json(name = "sla_quantity")
            val sla_quantity: Int,

            @Json(name = "cust_only")
            val cust_only: Boolean? = false,

            @Json(name = "extensions")
            val extensions: ProductExtensionsDto? = null,

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
