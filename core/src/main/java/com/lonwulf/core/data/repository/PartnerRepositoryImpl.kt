package com.lonwulf.core.data.repository

import com.lonwulf.core.analytics.FirebaseAnalyticsHelper
import com.lonwulf.core.data.CoreRemoteDataSource
import com.lonwulf.core.data.response.delivery.DeliveryConfigDto
import com.lonwulf.core.data.response.delivery.HolidayDto
import com.lonwulf.core.data.response.partner.PartnerDetailsDto
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.repository.PartnerRepository
import com.lonwulf.core.mapper.PartnerMapper
import com.lonwulf.core.objectboxdb.services.ProductService
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Result
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

class PartnerRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcherProvider,
    private val coreRemoteDataSource: CoreRemoteDataSource,
    private val firebaseAnalyticsHelper: FirebaseAnalyticsHelper,
    private val pseudoCategoryService: PseudoCategoryService,
    private val productService: ProductService,
) : PartnerRepository {
    private val pseudoCategoryMapper = PseudoCategoryMapper()

    override suspend fun fetchPartnerDetails(): Result<PartnerDetailsDto> {
        return when (val apiResult = coreRemoteDataSource.fetchPartnerDetails(dispatcher.io)) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                PartnerMapper.savePartnerData(apiResult.data)
                Result.Success(apiResult.data)
            }

            is Result.Error -> {
                Timber.e(apiResult.toString())
                firebaseAnalyticsHelper.logAppError(
                    apiResult.errorMessage ?: "error",
                    apiResult.code ?: 0
                )
                Result.Error(apiResult.cause, apiResult.code, apiResult.errorMessage)
            }
        }
    }

}
