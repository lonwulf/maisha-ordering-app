package com.lonwulf.cart.domain.usecase

import com.lonwulf.core.data.response.AgentSearchDto
import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.core.utils.Result
import com.lonwulf.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AgentSearchUseCase @Inject constructor(private val repository: CartRepository) {
    operator fun invoke(
        agentName: String,
        agentPhone: String
    ): Flow<GenericApiResult<AgentSearchDto?>> = flow {
        when (val result = repository.agentSearch(agentName, agentPhone)) {
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
