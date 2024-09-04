package com.lonwulf.core.domain.usecase

import com.lonwulf.core.domain.repository.DataRepository
import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.core.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 *  ApiFetch for products and insertion to DB
 *
 * @property repository
 */
class FetchProductsApiUseCase @Inject constructor(private val repository: DataRepository) {

    operator fun invoke(): Flow<GenericApiResult<Any>> = flow {
        when (val result = repository.fetchAndInsertProducts()) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                repository.fetchDeactivatedProducts()
                emit(GenericApiResult(results = result.data, success = true, message = ""))
            }

            is Result.Error -> {
                emit(
                    GenericApiResult(
                        results = Any(),
                        success = false,
                        message = result.errorMessage ?: ""
                    )
                )
            }
        }
    }
}
