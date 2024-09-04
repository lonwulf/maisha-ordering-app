package com.lonwulf.core.domain.usecase

import com.lonwulf.core.domain.repository.DataRepository
import com.lonwulf.core.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchCustomerStatusUseCase @Inject constructor(private val repository: DataRepository) {
    suspend operator fun invoke(): Flow<Result<Boolean>> = flow {
        emit(repository.fetchCustomerStatus())
    }
}
