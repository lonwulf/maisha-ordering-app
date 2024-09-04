package com.lonwulf.cart.domain.usecase

import com.lonwulf.core.domain.models.Product
import javax.inject.Inject

class DeleteNonJncProductsUseCase @Inject constructor(private val cartCacheDataSource: ShoppingCartCacheDataSource) {
    suspend operator fun invoke(productArray: Array<Product>) {
        cartCacheDataSource.deleteNonJNCProducts(productArray)
    }
}
