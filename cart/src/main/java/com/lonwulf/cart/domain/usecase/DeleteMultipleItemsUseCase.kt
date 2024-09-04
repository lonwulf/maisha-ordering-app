package com.lonwulf.cart.domain.usecase

class DeleteMultipleItemsUseCase(private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource) {
    suspend operator fun invoke() {
        shoppingCartCacheDataSource.deleteAllCartItems()
    }
}
