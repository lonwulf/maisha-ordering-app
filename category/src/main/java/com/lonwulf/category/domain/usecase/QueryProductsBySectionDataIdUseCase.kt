package com.lonwulf.category.domain.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.category.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QueryProductsBySectionDataIdUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(sectionId: Int): Flow<List<Product>> = flow {
        emit(
            repository.getProductsBySectionDataId(sectionId)
        )
    }
}
