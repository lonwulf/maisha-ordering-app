package com.lonwulf.core.data.mappers

import com.lonwulf.core.data.response.catalog.CategoriesResponseDto
import com.lonwulf.core.database.entity.Image
import com.lonwulf.core.domain.models.Category
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.domain.models.PseudoCategory
import com.lonwulf.core.objectboxdb.entity.CartCacheEntityOB
import com.lonwulf.core.objectboxdb.entity.CategoryEntityOB

fun CategoriesResponseDto.mapApiResponseToCategoryEntity(): List<CategoryEntityOB> {
    val categories = mutableListOf<CategoryEntityOB>()
    this.data?.categories?.map { category ->
        category.parent_id?.let {
            categories.add(
                CategoryEntityOB(
                    categoryId = category.id ?: 0,
                    name = category.name ?: "",
                    displayName = category.display_name ?: "",
                    priority = category.priority ?: 0,
                    videoActive = false,
                    featured = category.featured ?: false,
                    parentId = category.parent_id,
                    color = null,
                    image = setImage(category.images ?: listOf()),
                    icon = null,
                    video = null
                )
            )
        }
    }
    return categories
}

private fun setImage(images: List<CategoriesResponseDto.Data.Categories.Images>): Image? {
    var image: Image? = null
    if (images.isNotEmpty()) {
        images.filter { it.position == 0 }
            .forEach { // add to image if position is 0, otherwise stop
                image = Image(it.position, it.url)
            }
        if (image == null) {
            image = Image(images.last().position, images.last().url)
        }
    }
    return image
}

fun CategoryEntityOB.mapCategoryDataToDomain(): Category {
    return Category(
        id = this.categoryId,
        name = this.name,
        priority = this.priority,
        parentId = this.parentId,
        imageUrl = this.image.let {
            it?.url
        },
        displayName = displayName,
        featured = featured
    )
}

fun Product.mapProductToCartCacheEntity():CartCacheEntityOB {
    return CartCacheEntityOB(
        item_code = this.code,
        image_url = this.imageUrl ?: "",
        item_name = this.name,
        item_price = this.price,
        item_old_price = this.oldPrice,
        item_saving = this.savings,
        item_favorite = this.favorite,
        item_quantity = this.quantity,
        isProductJNC = this.isProductJnc,
        maxSaleQty = this.maxSaleQty,
        minSaleQty = this.minSaleQty,
        categoryId = this.categoryId,
        productId = this.id.toInt(),
        saleDelay = this.saleDelay,
        slaDays = this.slaDays,
        slaQuantity = this.slaQuantity
    )
}

fun PseudoCategoryEntityOB.mapPseudoCategoryEntityToDomain(): PseudoCategory {
    return PseudoCategory(
        name = this.name,
        displayName = this.displayName,
        imageUrl = this.imageUrl
    )
}