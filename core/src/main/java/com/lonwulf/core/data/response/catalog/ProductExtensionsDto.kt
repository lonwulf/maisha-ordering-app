package com.lonwulf.core.data.response.catalog

data class ProductExtensionsDto(
    val search_terms: String? = null,
    val promote: Boolean? = false,
    val top: Boolean? = false,
    val app_only: Boolean? = false,
    val clearance: Boolean? = false,
    val all_desc: String? = null,
    val long_desc: String? = null,
)
