package com.lonwulf.category.domain.entities

import com.lonwulf.core.domain.models.Product

/**
 * Represent the current object for a category Item in the app,
 * Object creation uses the builder pattern.
 *
 * @property id
 * @property name category name for display
 * @property priority display position in view
 * @property parentId if null its MainCategory otherwise its SubCategory
 * @property imageUrl display image
 * @property section_data Icon to display (confirm if needed)
 * @property videoUrl associated video for category
 * @property color
 */
data class SectionData(
    var id: Int?,
    var sectionId: Int?,
    var active: Boolean?,
    var priority: Int?,
    var title: String?,
    var image: String?,
    var productsData: List<Product>?
)
