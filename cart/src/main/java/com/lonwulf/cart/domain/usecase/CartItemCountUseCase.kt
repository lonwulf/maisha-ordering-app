package com.lonwulf.cart.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CartItemCountUseCase(private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource) {
    suspend operator fun invoke(): Flow<Int> = flow {
        val result = shoppingCartCacheDataSource.getCartItemCount()
        emit(result)
    }
}
