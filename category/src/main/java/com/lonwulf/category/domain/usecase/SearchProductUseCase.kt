package com.lonwulf.category.domain.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.category.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Get list of products to use within the app display screen
 * For SubCategories pick first six items sorted by priority
 *
 */
class SearchProductUseCase @Inject constructor(private val repository: CategoryRepository) {

    suspend operator fun invoke(): Flow<List<Product>> = flow {
        val productItems = mutableListOf<Product>()
        productItems.addAll(repository.findAllProducts())
        emit(productItems)
    }
}
