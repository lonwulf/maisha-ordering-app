package com.lonwulf.cart.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteAnItemUseCase(
    private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
) {

    suspend operator fun invoke(productCode: String): Flow<Int> = flow {
        val result = shoppingCartCacheDataSource.deleteCartItem(productCode)
        emit(result)
    }
}
