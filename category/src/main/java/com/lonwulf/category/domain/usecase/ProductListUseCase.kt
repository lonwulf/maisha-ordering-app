package com.lonwulf.category.domain.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.category.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductListUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(): Flow<List<Product>> = flow {
        val productItems = mutableListOf<Product>()
        productItems.addAll(categoryRepository.findAllProducts())
        emit(productItems)
    }

    suspend fun getProductsSet(): Flow<Set<Product>> = flow {
        val productItems = mutableListOf<Product>()
        productItems.addAll(categoryRepository.findAllProducts())
        emit(productItems.toSet())
    }
}
