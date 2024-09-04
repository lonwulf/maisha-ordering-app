package com.lonwulf.cart.domain.usecase

import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.core.utils.Result
import com.lonwulf.cart.data.response.AgentMarkersDto
import com.lonwulf.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AgentsOnMapUseCase @Inject constructor(private val repository: CartRepository) {
    operator fun invoke(
        county: String,
        longitude: Double,
        latitude: Double
    ): Flow<GenericApiResult<AgentMarkersDto?>> = flow {
        when (val result = repository.agentOnMap(county, longitude, latitude)) {
            is Result.Success -> {
                emit(GenericApiResult(results = result.data, success = true, message = "success"))
            }
            is Result.Error -> {
                emit(
                    GenericApiResult(
                        results = null,
                        success = false,
                        message = result.errorMessage ?: "something went wrong"
                    )
                )
            }
            is Result.Loading -> Result.Loading
        }
    }
}
