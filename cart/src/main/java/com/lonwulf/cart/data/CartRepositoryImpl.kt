@file:Suppress("TooManyFunctions", "PackageName")

package com.lonwulf.cart.data

import com.lonwulf.core.analytics.FirebaseAnalyticsHelper
import com.lonwulf.core.data.mappers.ReceivingAgentMapper
import com.lonwulf.core.data.response.AgentSearchDto
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.objectboxdb.services.PreviousRAService
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Prefs
import com.lonwulf.core.utils.Result
import com.lonwulf.cart.data.mapper.mapToCouponModel
import com.lonwulf.cart.data.mapper.mapToCouponProductDTO
import com.lonwulf.cart.data.response.AgentMarkersDto
import com.lonwulf.cart.data.response.AgentSearchDto
import com.lonwulf.cart.data.response.NearestAgentDTO
import com.lonwulf.cart.data.response.ProcessOrderDto
import com.lonwulf.cart.data.response.ValidateQuantityDto
import com.lonwulf.cart.data.source.CartModuleRemoteDataSource
import com.lonwulf.cart.domain.model.CouponModel
import com.lonwulf.cart.domain.model.CouponProductDataModel
import com.lonwulf.cart.domain.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcherProvider,
    private val cartModuleDataSource: CartModuleRemoteDataSource,
    private val previousRAService: PreviousRAService,
    private val receivingAgentMapper: ReceivingAgentMapper
) : CartRepository {
    private val companyId by lazy {
        Prefs.getInt(Constant.MultiCompanyKeys.COUNTRY_ID)
    }

    override suspend fun confirmOrder(
        agentId: Long,
        sms: String,
        partnerType: String,
        amount: Double,
        phoneToPay: String
    ): Result<ProcessOrderDto> {
        Timber.e(javaClass.simpleName.plus(" ").plus("Place Order API").plus(" "))
        return when (
            val apiResult =
                cartModuleDataSource.processOrder(
                    dispatcher.io,
                    agentId,
                    sms,
                    amount,
                    phoneToPay
                )
        ) {
            is Result.Loading -> Result.Loading
            is Result.Success<ProcessOrderDto> -> {
                Result.Success(apiResult.data)
            }

            is Result.Error -> {
                firebaseAnalyticsHelper.logAppError(
                    apiResult.errorMessage ?: "error",
                    apiResult.code ?: 0
                )
                Result.Error(apiResult.cause, apiResult.code, apiResult.errorMessage)
            }
        }
    }

    override suspend fun agentSearch(
        agentName: String,
        agentPhone: String
    ): Result<AgentSearchDto> {
        return when (
            val apiResult =
                cartModuleDataSource.agentSearch(
                    dispatcher.io,
                    agentName,
                    agentPhone
                )
        ) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                Result.Success(apiResult.data)
            }

            is Result.Error -> {
                firebaseAnalyticsHelper.logAppError(
                    apiResult.errorMessage ?: "error",
                    apiResult.code ?: 0
                )
                Result.Error(apiResult.cause, apiResult.code, apiResult.errorMessage)
            }
        }
    }


    override suspend fun agentOnMap(
        county: String,
        longitude: Double,
        latitude: Double
    ): Result<AgentMarkersDto> {
        return when (
            val apiResult =
                cartModuleDataSource.agentOnMap(
                    dispatcher.io,
                    county,
                    longitude,
                    latitude
                )
        ) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                Result.Success(apiResult.data)
            }

            is Result.Error -> {
                firebaseAnalyticsHelper.logAppError(
                    apiResult.errorMessage ?: "error",
                    apiResult.code ?: 0
                )
                Result.Error(apiResult.cause, apiResult.code, apiResult.errorMessage)
            }
        }
    }

    override suspend fun validateCoupons(
        coupon: String,
        partnerId: Long,
        products: List<CouponProductDataModel>,
        categoryIds: ArrayList<Int>
    ): Result<CouponModel> {
        val mappedProduct = products.mapToCouponProductDTO()

        return when (
            val apiResult = cartModuleDataSource.validateCoupons(
                dispatcher.io,
                coupon,
                partnerId,
                products = mappedProduct,
                categoryIds,
                companyId
            )
        ) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                val result = apiResult.data.mapToCouponModel()
                Result.Success(result)
            }

            is Result.Error -> {
                firebaseAnalyticsHelper.logAppError(
                    apiResult.errorMessage ?: "error",
                    apiResult.code ?: 0
                )
                Result.Error(apiResult.cause, apiResult.code, apiResult.errorMessage)
            }
        }
    }

    override suspend fun validateQuantity(productIds: List<String>): Result<ValidateQuantityDto> {
        return when (
            val response = cartModuleDataSource.validateProductQuantity(
                dispatcher = Dispatchers.IO,
                productIds = productIds
            )
        ) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                Result.Success(response.data)
            }

            is Result.Error -> Result.Error(response.cause, response.code, response.errorMessage)
        }
    }

    override suspend fun fetchNearByAgents(
        latitude: Double,
        longitude: Double,
        radius: Int
    ): Result<NearestAgentDTO> {
        return when (
            val apiResult = cartModuleDataSource.fetchNearByAgents(
                dispatcher.io,
                latitude,
                longitude,
                radius
            )
        ) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                Result.Success(apiResult.data)
            }

            is Result.Error -> {
                firebaseAnalyticsHelper.logAppError(
                    apiResult.errorMessage ?: "error",
                    apiResult.code ?: 0
                )
                Result.Error(apiResult.cause, apiResult.code, apiResult.errorMessage)
            }
        }
    }
}
