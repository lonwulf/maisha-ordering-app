@file:Suppress("PackageNaming")

package com.lonwulf.cart.domain.usecase

import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.core.utils.Result
import com.lonwulf.cart.data.mapper.toNearestAgentModel
import com.lonwulf.cart.domain.model.NearestAgentModel
import com.lonwulf.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchNearestAgentUseCase @Inject constructor(private val repository: CartRepository) {

    operator fun invoke(
        latitude: Double,
        longitude: Double,
        radius: Int
    ): Flow<GenericApiResult<List<NearestAgentModel?>>> = flow {
        when (val result = repository.fetchNearByAgents(latitude, longitude, radius)) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                emit(
                    GenericApiResult(
                        results = result.data.data?.toNearestAgentModel() ?: emptyList(),
                        success = true,
                        message = "success"
                    )
                )
            }

            is Result.Error -> {
                emit(
                    GenericApiResult(
                        results = emptyList(),
                        success = false,
                        message = result.errorMessage ?: "something went wrong"
                    )
                )
            }
        }
    }
}
