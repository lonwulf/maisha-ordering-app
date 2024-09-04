package com.lonwulf.core.domain.models

data class SectionData(
    var id: Int?,
    var sectionId: Int?,
    var active: Boolean?,
    var priority: Int?,
    var title: String?,
    var image: String?,
    var productsData: List<Product>?,
)
