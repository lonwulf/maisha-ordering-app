package com.lonwulf.core.domain.usecase

import com.lonwulf.core.data.mappers.mapSectionDataToDomain
import com.lonwulf.core.domain.models.Section
import com.lonwulf.core.domain.repository.DataRepository
import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.core.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchHomePageApiUseCase @Inject constructor(private val repository: DataRepository) {
    suspend operator fun invoke(): Flow<GenericApiResult<List<Section>>> = flow {
        when (val result = repository.fetchHomePage()) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                val fetchInsertResult = result.data
                val sectionsItems = fetchInsertResult
                    .entries
                    .map { it.mapSectionDataToDomain(repository.getSectionProductsByIds(it.value)) }
                    .toList()
                    .sortedBy { it.priority }

                val apiResult = GenericApiResult(
                    results = sectionsItems,
                    success = true,
                    message = "successful"
                )
                emit(apiResult)
            }

            is Result.Error -> {
                val apiResult = GenericApiResult(
                    results = emptyList<Section>(),
                    success = false,
                    message = result.errorMessage ?: "something went wrong"
                )
                emit(apiResult)
            }
        }
    }
}
