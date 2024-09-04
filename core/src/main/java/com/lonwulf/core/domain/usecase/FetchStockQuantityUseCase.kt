@file:Suppress("PackageNaming")

package com.lonwulf.core.domain.usecase

import com.lonwulf.core.domain.repository.DataRepository
import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.core.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchStockQuantityUseCase @Inject constructor(private val repository: DataRepository) {
    suspend operator fun invoke(): Flow<GenericApiResult<Boolean>> = flow {
        when (val result = repository.fetchProductStockQuantity()) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                emit(GenericApiResult(results = result.data, success = true, message = "success"))
            }

            is Result.Error -> {
                emit(
                    GenericApiResult(
                        results = false,
                        success = false,
                        message = result.errorMessage ?: "error"
                    )
                )
            }
        }
    }
}
