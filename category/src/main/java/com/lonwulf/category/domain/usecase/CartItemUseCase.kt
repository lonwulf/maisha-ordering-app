package com.lonwulf.category.domain.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.category.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartItemUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(item: Product): Flow<Long> = flow {
        emit(repository.insertItemToCart(item))
    }

    suspend fun getCartTotal(): Flow<Int> = flow {
        emit(repository.getCartCount())
    }

    fun updateItemQuantity(
        productCode: String,
        quantity: Int
    ): Flow<Int> = flow {
        emit(repository.updateItemQuantity(productCode, quantity))
    }
}
