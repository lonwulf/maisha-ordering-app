package com.lonwulf.core.domain.usecase

import com.lonwulf.core.data.response.partner.PartnerDetailsDto
import com.lonwulf.core.domain.repository.PartnerRepository
import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.core.utils.Result
import com.lonwulf.core.utils.exhaustive
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchPartnerDetailsUseCase @Inject constructor(private val partnerRepository: PartnerRepository) {
    suspend operator fun invoke() = flow<GenericApiResult<PartnerDetailsDto?>> {
        val data = partnerRepository.fetchPartnerDetails()
        when (data) {
            is Result.Error -> {
                val genericApiResult = GenericApiResult(
                    results = null,
                    success = false,
                    message = data.errorMessage ?: "Something went wrong! Please try again."
                )
                emit(genericApiResult)
            }
            Result.Loading -> Result.Loading
            is Result.Success -> {
                val genericApiResult = GenericApiResult(
                    results = null,
                    success = true,
                    message = "Success"
                )
                emit(genericApiResult)
            }
        }.exhaustive
    }
}
