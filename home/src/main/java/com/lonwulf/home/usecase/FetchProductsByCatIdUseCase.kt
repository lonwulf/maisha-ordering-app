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
class FetchProductsByCatIdUseCase @Inject constructor(private val repository: HomePageRepository) {

    suspend operator fun invoke(categoryId: Int): Flow<List<Product>> = flow {
        val productItems = mutableListOf<Product>()
        val items = repository.getProductsByCategoryId(categoryId)
        productItems.addAll(items)

        emit(productItems)
    }
}
