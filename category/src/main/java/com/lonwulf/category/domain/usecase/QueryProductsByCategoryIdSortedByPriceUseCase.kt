package com.lonwulf.category.domain.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.category.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QueryProductsByCategoryIdSortedByPriceUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(
        categoryId: Int,
        isAscending: Int
    ): Flow<List<Product>> = flow {
        emit(
            repository.getProductsByCategoryIdSortedByPrice(
                categoryId = categoryId,
                isAscending = isAscending
            )
        )
    }
}
