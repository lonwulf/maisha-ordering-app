package com.lonwulf.home.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.home.domain.repositories.HomePageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Get list of products to use within the app display screen
 * For SubCategories pick first six items sorted by priority
 *
 */
class FetchProductsByCatIdsUseCase @Inject constructor(private val repository: HomePageRepository) {
    suspend operator fun invoke(categoryIds: IntArray): Flow<List<Product>> = flow {
        val productItems = mutableListOf<Product>()
        val items = repository.getRecommendedProductsByCategoryId(categoryIds)
        productItems.addAll(items)
        val shuffledProductItems: MutableList<Product> = productItems.shuffled().toMutableList()
        emit(shuffledProductItems)
    }
}
