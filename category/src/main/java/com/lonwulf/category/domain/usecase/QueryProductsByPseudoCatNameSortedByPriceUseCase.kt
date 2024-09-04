package com.lonwulf.category.domain.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.category.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QueryProductsByPseudoCatNameSortedByPriceUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(
        pseudoCategoryName: String,
        isAscending: Int
    ): Flow<List<Product>> = flow {
        emit(
            repository.getProductsByPseudoCategoryNameSortedByPrice(
                pseudoCategoryName = pseudoCategoryName,
                isAscending = isAscending
            )
        )
    }
}
