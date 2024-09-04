package com.lonwulf.category.domain.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.category.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 *  ApiFetch for products and insertion to DB
 *
 * @property repository
 */
class FavoriteProductUseCase @Inject constructor(private val repository: CategoryRepository) {

    suspend operator fun invoke(product: Product): Flow<Product> = flow {
        emit(repository.updateProductFavourite(product))
    }
}
