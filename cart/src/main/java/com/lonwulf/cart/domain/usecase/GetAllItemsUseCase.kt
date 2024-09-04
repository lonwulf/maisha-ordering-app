package com.lonwulf.cart.domain.usecase

import com.lonwulf.core.domain.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllItemsUseCase @Inject constructor(private val shoppingCartCacheDataSource: ShoppingCartCacheDataSource) {
    suspend operator fun invoke(forceToShowPromoPrices: Boolean): Flow<List<Product>> = flow {
        val result = shoppingCartCacheDataSource.getCartItems(forceToShowPromoPrices)
        emit(result)
    }
}
