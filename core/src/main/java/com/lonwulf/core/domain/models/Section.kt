package com.lonwulf.core.domain.models

import androidx.paging.PagedList

data class Section(
    val id: Int? = 0,
    val active: Boolean? = false,
    val viewType: String? = "",
    val priority: Int? = 0,
    val sectionTitle: String? = "",
    var sectionData: List<SectionData>? = listOf(),
    val categories: List<CategoryItems>? = listOf(),
    val popularProducts: List<Product>? = listOf(),
    val products: PagedList<Product>? = null,
    val pseudoCategories: List<PseudoCategory>? = listOf()
)
