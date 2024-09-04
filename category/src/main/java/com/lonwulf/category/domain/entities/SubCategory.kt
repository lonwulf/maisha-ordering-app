package com.lonwulf.category.domain.entities

// TODO: Delete this asap and use category
/**
 * Rep SubCategories within main category
 *
 * @property id: Used to query for Products
 * @property subtitle: SubCategory name
 * @property imageUrl: Image to show
 */
data class SubCategory(
    val id: Int,
    val subtitle: String,
    val subCategoryListItems: List<com.lonwulf.core.domain.models.Category>
)

/**
 * Object item representing list of items within a given subcategory
 *
 * @property id property identifier
 * @property name item name
 * @property imageUrl image to display
 */
// data class SubCategoryItem(val id: Int, val name: String, val imageUrl: String?)
