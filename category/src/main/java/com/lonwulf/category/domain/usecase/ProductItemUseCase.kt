package com.lonwulf.category.domain.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.category.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Allows user to get a product item
 *
 * @property repository
 */
class ProductItemUseCase @Inject constructor(private val repository: CategoryRepository) {

    suspend operator fun invoke(productCode: String): Flow<GenericApiResult<Product?>> = flow {
        val product = repository.getProductByCode(productCode)
        if (product != null) {
            val result = GenericApiResult(results = product, success = true, message = "success fetching")
            emit(result)
        } else {
            val result = GenericApiResult(results = null, success = false, message = "error fetching product")
            emit(result)
        }
    }
}
