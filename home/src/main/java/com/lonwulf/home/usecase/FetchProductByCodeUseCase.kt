package com.lonwulf.home.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.home.domain.repositories.HomePageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Allows user to get a product item
 *
 * @property repository
 */
class FetchProductByCodeUseCase @Inject constructor(private val repository: HomePageRepository) {

    suspend operator fun invoke(productCode: String): Flow<Product?> = flow {
        emit(repository.getProductByCode(productCode))
    }
}
