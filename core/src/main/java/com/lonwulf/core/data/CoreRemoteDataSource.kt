package com.lonwulf.core.data

import com.lonwulf.core.BuildConfig
import com.lonwulf.core.data.request.DeactivatedProductsRequestBody
import com.lonwulf.core.data.request.HomePageRequestBody
import com.lonwulf.core.data.request.PromoRequestBody
import com.lonwulf.core.data.response.catalog.CategoriesResponseDto
import com.lonwulf.core.data.response.catalog.DeactivatedProductsDto
import com.lonwulf.core.data.response.catalog.HomePageResponseDto
import com.lonwulf.core.data.response.catalog.ProductQuantityResponseDto
import com.lonwulf.core.data.response.catalog.ProductsResponseDto
import com.lonwulf.core.data.response.catalog.PromoResponseDto
import com.lonwulf.core.data.response.delivery.DeliveryConfigDto
import com.lonwulf.core.data.response.delivery.HolidayDto
import com.lonwulf.core.data.response.delivery.HomeDeliveryDto
import com.lonwulf.core.data.response.partner.AgentDto
import com.lonwulf.core.data.response.partner.PartnerDetailsDto
import com.lonwulf.core.network.ApiResponse
import com.lonwulf.core.network.RemoteDataSource
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Prefs
import com.lonwulf.core.utils.Result
import com.lonwulf.core.utils.UrlSwitcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CoreRemoteDataSource @Inject constructor(private val coreService: CoreService) :
    RemoteDataSource() {

    suspend fun fetchPartnerDetails(dispatcher: CoroutineDispatcher): Result<PartnerDetailsDto> {
        return safeApiCall(dispatcher) {
            coreService.fetchPartnerDetails(
                BodyRequest(
                    Prefs.getLong(Constant.PrefKeys.PARTNER_ID)
                )
            )
        }
    }


    suspend fun fetchPromosFromConfig(dispatcher: CoroutineDispatcher): Result<PromoResponseDto> {
        return safeApiCall(dispatcher) {
            coreService.fetchPromosFromConfig(UrlSwitcher.getPromotionsUrl)
        }
    }

    suspend fun fetchChannelSpecificPromos(
        dispatcher: CoroutineDispatcher,
        channel: String
    ): Result<PromoResponseDto> {
        return safeApiCall(dispatcher) {
            coreService.fetchChannelSpecificPromos(
                PromoRequestBody(
                    BuildConfig.APP_ID,
                    0.0,
                    channel,
                    Prefs.getString(Constant.PrefKeys.PARTNER_ID)?.toIntOrNull() ?: 0
                )
            )
        }
    }


    suspend fun fetchProducts(dispatcher: CoroutineDispatcher): Result<ProductsResponseDto> {
        // already gone through with 1st sync: do delta sync else do full sync
        return safeApiCall(dispatcher) {
            if (Prefs.getDouble(Constant.PrefKeys.LAST_SYNC_TIME_STAMP) == 0.0) {
                coreService.fetchProducts(UrlSwitcher.getProductSyncUrl)
            } else {
                coreService.fetchProducts(UrlSwitcher.getDeltaProductSyncUrl)
            }
        }
    }

    suspend fun fetchDeactivatedProducts(dispatcher: CoroutineDispatcher): Result<DeactivatedProductsDto> {
        return safeApiCall(dispatcher) {
            coreService.fetchDeactivatedProducts(
                DeactivatedProductsRequestBody(
                    app_id = BuildConfig.APP_ID.toInt(),
                    last_sync_date = Prefs.getDouble(Constant.PrefKeys.DEACTIVATED_LAST_SYNC_TIME_STAMP),
                    company_id = Prefs.getInt(Constant.MultiCompanyKeys.COUNTRY_ID)
                )
            )
        }
    }

    suspend fun fetchProductQuantity(dispatcher: CoroutineDispatcher): Result<ProductQuantityResponseDto> {
        return safeApiCall(dispatcher = dispatcher) {
            coreService.fetchProductQuantity(UrlSwitcher.getProductQuantityUrl)
        }
    }

    suspend fun fetchHomePage(dispatcher: CoroutineDispatcher): Result<HomePageResponseDto> {
        return safeApiCall(dispatcher) {
            coreService.fetchHomePage(
                HomePageRequestBody(
                    BuildConfig.APP_ID,
                    0.0,
                    Prefs.getInt(Constant.MultiCompanyKeys.COUNTRY_ID),
                    partnerType
                )
            )
        }
    }


    suspend fun fetchCategories(dispatcher: CoroutineDispatcher): Result<CategoriesResponseDto> {
        return safeApiCall(dispatcher) {
            coreService.fetchCategories(
                MissingCompanyIdRequestBody(
                    BuildConfig.APP_ID,
                    0.0,
                    Prefs.getInt(Constant.MultiCompanyKeys.COUNTRY_ID),
                    partnerType
                )
            )
        }
    }

    suspend fun fetchCustomerStatus(dispatcher: CoroutineDispatcher): Result<CustomerStatusResponseDto> {
        return safeApiCall(dispatcher) {
            coreService.fetchCustomerStatus(
                Prefs.getLong(Constant.PrefKeys.PARTNER_ID)
            )
        }
    }


}
