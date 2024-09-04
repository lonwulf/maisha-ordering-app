@file:Suppress("PackageNaming")

package com.lonwulf.cart.domain.usecase

import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.core.utils.Result
import com.lonwulf.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ValidateQuantityUseCase @Inject constructor(private val repository: CartRepository) {
    suspend operator fun invoke(productIds: List<String>): Flow<GenericApiResult<ValidateQuantityDto?>> =
        flow {
            when (val response = repository.validateQuantity(productIds = productIds)) {
                is Result.Loading -> Result.Loading
                is Result.Success -> {
                    emit(
                        GenericApiResult(
                            results = response.data,
                            success = true,
                            message = "success"
                        )
                    )
                }

                is Result.Error -> {
                    emit(
                        GenericApiResult(
                            results = null,
                            success = false,
                            message = "Something went wrong"
                        )
                    )
                }
            }
        }
}
