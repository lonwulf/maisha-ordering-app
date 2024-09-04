@file:Suppress("TooManyFunctions")

package com.lonwulf.cart.data.source

import com.lonwulf.core.data.response.AgentSearchDto
import com.lonwulf.core.network.RemoteDataSource
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Constant.APP_ERP_ID
import com.lonwulf.core.utils.Prefs
import com.lonwulf.core.utils.Result
import com.lonwulf.core.utils.UrlSwitcher
import com.lonwulf.cart.data.CartService
import com.lonwulf.cart.data.CartService.BodyRequest
import com.lonwulf.cart.data.response.AgentMarkersDto
import com.lonwulf.cart.data.response.CouponDTO
import com.lonwulf.cart.data.response.ProcessOrderDto
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Provides access to making api calls by various endpoints for
 * Product Order Placement
 * @property cartService: Retrofit instance service with related endpoints
 */
class CartModuleRemoteDataSource(private val cartService: CartService) : RemoteDataSource() {

    suspend fun processOrder(
        dispatcher: CoroutineDispatcher,
        agentId: Long,
        sms: String,
        amount: Double,
        phoneToPay: String
    ): Result<ProcessOrderDto> {
        return safeApiCall(dispatcher) {
            cartService.processOrder(
                agentId,
                BodyRequest(
                    APP_ERP_ID.toInt(),
                    Prefs.getInt(Constant.MultiCompanyKeys.COUNTRY_ID),
                    sms,
                    Constant.SmsKeyWords.COPIA_SMS_SHORTCODE,
                    amount.toInt(),
                    phoneToPay
                )
            )
        }
    }

    suspend fun getCounties(dispatcher: CoroutineDispatcher): Result<CountyDto> {
        return safeApiCall(dispatcher) {
            cartService.getCounties(
                CartService.CountyRequestBody(
                    company_id = Prefs.getInt(Constant.MultiCompanyKeys.COUNTRY_ID)
                )
            )
        }
    }

    suspend fun getTowns(dispatcher: CoroutineDispatcher, county: String): Result<TownDto> {
        return safeApiCall(dispatcher) {
            cartService.getTowns(
                CartService.TownRequestBody(
                    company_id = Prefs.getInt(Constant.MultiCompanyKeys.COUNTRY_ID),
                    county = county
                )
            )
        }
    }

    suspend fun getVillages(dispatcher: CoroutineDispatcher, townId: Int): Result<VillageDto> {
        return safeApiCall(dispatcher) {
            cartService.getVillages(
                CartService.VillageRequestBody(
                    company_id = Prefs.getInt(Constant.MultiCompanyKeys.COUNTRY_ID),
                    town_id = townId
                )
            )
        }
    }

    suspend fun agentSearch(
        dispatcher: CoroutineDispatcher,
        agentName: String,
        agentPhone: String,
    ): Result<AgentSearchDto> {
        return safeApiCall(dispatcher) {
            cartService.agentSearch(
                CartService.AgentSearchPayload(agentName, agentPhone)
            )
        }
    }

    suspend fun villageAgents(
        dispatcher: CoroutineDispatcher,
        companyId: Int,
        villageId: Int,
        page: Int
    ): Result<AgentSearchDto> {
        return safeApiCall(dispatcher) {
            cartService.villageAgents(
                CartService.VillageAgentsPayload(companyId, villageId, page)
            )
        }
    }

    suspend fun townAgents(
        dispatcher: CoroutineDispatcher,
        companyId: Int,
        townId: Int
    ): Result<AgentSearchDto> {
        return safeApiCall(dispatcher) {
            cartService.townAgents(
                CartService.TownAgentsPayload(companyId, townId)
            )
        }
    }

    suspend fun agentOnMap(
        dispatcher: CoroutineDispatcher,
        county: String,
        longitude: Double,
        latitude: Double
    ): Result<AgentMarkersDto> {
        return safeApiCall(dispatcher) {
            cartService.markAgentsOnMap(
                UrlSwitcher.getNearestAgentUrl,
                CartService.MarkersPayload(county, longitude, latitude)
            )
        }
    }

    @SuppressWarnings("LongParameterList")
    suspend fun validateCoupons(
        dispatcher: CoroutineDispatcher,
        coupon: String,
        partnerId: Long,
        products: List<CartService.CouponProduct>,
        categoryIds: ArrayList<Int>,
        companyId: Int
    ): Result<CouponDTO> {
        return safeApiCall(dispatcher) {
            cartService.validateCoupon(
                CartService.CouponRequestBody(
                    coupon,
                    partnerId,
                    products,
                    categoryIds,
                    companyId
                )
            )
        }
    }

    suspend fun validateProductQuantity(
        dispatcher: CoroutineDispatcher,
        productIds: List<String>
    ): Result<ValidateQuantityDto> {
        return safeApiCall(dispatcher = dispatcher) {
            cartService.validateProductQuantity(
                CartService.ValidateQuantityBody(product_ids = productIds),
                UrlSwitcher.getValidateProductQuantityUrl
            )
        }
    }

    suspend fun fetchNearByAgents(
        dispatcher: CoroutineDispatcher,
        latitude: Double,
        longitude: Double,
        radius: Int
    ): Result<NearestAgentDTO> {
        return safeApiCall(dispatcher) {
            cartService.fetchNearByAgents(
                url = UrlSwitcher.getNearestAgentUrl,
                requestBody =
                CartService.NearestAgentsBody(
                    location = CartService.Coordinates(latitude = latitude, longitude = longitude),
                    radiusInMeter = radius
                )
            )
        }
    }
}
