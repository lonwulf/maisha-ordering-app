@file:Suppress("PackageNaming")

package com.lonwulf.home.usecase

import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.core.utils.Result
import com.lonwulf.home.data.response.LocationDto
import com.lonwulf.home.domain.repositories.HomePageRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateCustomerLocationUseCase @Inject constructor(private val repository: HomePageRepository) {
    suspend operator fun invoke(coordinates: LatLng): Flow<GenericApiResult<LocationDto?>> = flow {
        when (val response = repository.updateCustomerLocation(coordinates)) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                emit(GenericApiResult(results = response.data, success = true, message = "Success"))
            }

            is Result.Error -> {
                emit(
                    GenericApiResult(
                        results = null,
                        success = false,
                        message = response.errorMessage ?: "Something went wrong"
                    )
                )
            }
        }
    }
}
