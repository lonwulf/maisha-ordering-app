package com.lonwulf.cart.domain.usecase

class UpdateQuantityUseCase(private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource) {
    suspend operator fun invoke(productCode: String, qty: Int) {
        shoppingCartCacheDataSource.updateItemQuantity(productCode, qty)
    }
}
