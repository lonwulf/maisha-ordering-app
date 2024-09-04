package com.lonwulf.cart.domain.usecase

import com.lonwulf.core.domain.models.Product
import javax.inject.Inject

class InsertToCartUseCase @Inject constructor(
    private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource
) {
    suspend operator fun invoke(item: Product) {
        shoppingCartCacheDataSource.insertItemToCart(item = item)
    }
}
