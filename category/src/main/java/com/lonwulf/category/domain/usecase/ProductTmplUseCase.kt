package com.lonwulf.category.domain.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.category.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductTmplUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(tmplId: Int): Flow<List<Product>> = flow {
        val tmplProducts = repository.getAllTmplProducts(tmplId)
        if (tmplProducts.isNotEmpty()) {
            emit(tmplProducts)
        } else {
            emit(emptyList<Product>())
        }
    }
}
