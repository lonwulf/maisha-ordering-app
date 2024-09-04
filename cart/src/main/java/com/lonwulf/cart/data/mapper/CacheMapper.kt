package com.lonwulf.cart.data.mapper

import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.objectboxdb.entity.CartCacheEntityOB
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheMapper @Inject constructor() : EntityMapper<CartCacheEntityOB, Product> {

    override fun mapToEntity(model: Product): CartCacheEntityOB {
        return CartCacheEntityOB(
            item_code = model.code,
            image_url = model.imageUrl ?: "",
            item_name = model.name,
            item_price = model.price,
            item_old_price = model.oldPrice,
            item_saving = model.savings,
            item_favorite = model.favorite,
            item_quantity = model.quantity,
            isProductJNC = model.isProductJnc,
            maxSaleQty = model.maxSaleQty,
            minSaleQty = model.minSaleQty,
            productId = (model.id).toInt(),
            categoryId = model.categoryId,
            saleDelay = model.saleDelay,
            slaQuantity = model.slaQuantity,
            slaDays = model.slaDays
        )
    }
}
