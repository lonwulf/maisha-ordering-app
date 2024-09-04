package com.lonwulf.category.domain.entities

/**
 * Represent the current object for a category Item in the app,
 *
 * @property id
 * @property name category name for display
 * @property priority display position in view
 * @property parentId if null its MainCategory otherwise its SubCategory
 * @property imageUrl display image
 * @property iconUrl Icon to display (confirm if needed)
 * @property videoUrl associated video for category
 * @property color
 */
@Deprecated("Moved to core")
data class Category(
    val id: Int,
    val name: String = "",
    val priority: Int? = 0,
    val parentId: Int? = 0,
    val imageUrl: String? = "",
    val displayName: String = "",
    val featured: Boolean = false,
    val iconUrl: String? = "",
    val videoUrl: String? = "",
    val color: String? = ""
)

@Deprecated("Moved to core")
data class CategoryItems(
    var parentCategory: Category,
    var subCategory: List<Category>
)
