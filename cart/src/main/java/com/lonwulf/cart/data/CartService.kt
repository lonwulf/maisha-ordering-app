@file:Suppress("ConstructorParameterNaming", "TooManyFunctions", "PackageName")

package com.lonwulf.cart.data

import com.lonwulf.core.data.response.AgentSearchDto
import com.lonwulf.cart.data.response.AgentMarkersDto
import com.lonwulf.cart.data.response.AgentSearchDto
import com.lonwulf.cart.data.response.CouponDTO
import com.lonwulf.cart.data.response.NearestAgentDTO
import com.lonwulf.cart.data.response.ProcessOrderDto
import com.lonwulf.cart.data.response.ValidateQuantityDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface CartService {
    @POST("cms/v1/orders/{agent_id}/process")
    suspend fun processOrder(
        @Path("agent_id") agentId: Long,
        @Body bodyRequest: BodyRequest
    ): ProcessOrderDto

    @POST("cms/v1/locations/agent_search")
    suspend fun agentSearch(
        @Body bodyRequest: AgentSearchPayload
    ): AgentSearchDto

    @POST
    suspend fun markAgentsOnMap(
        @Url url: String,
        @Body bodyRequest: MarkersPayload
    ): AgentMarkersDto

    @POST("cms/v1/coupons/validate")
    suspend fun validateCoupon(@Body requestBody: CouponRequestBody): CouponDTO

    @POST
    suspend fun validateProductQuantity(
        @Body request: ValidateQuantityBody,
        @Url validateQuantityUrl: String
    ): ValidateQuantityDto

    @POST
    suspend fun fetchNearByAgents(
        @Body requestBody: NearestAgentsBody,
        @Url url: String
    ): NearestAgentDTO

    class CountyRequestBody(
        val company_id: Int
    )

    data class TownRequestBody(
        val company_id: Int,
        val county: String
    )

    data class VillageRequestBody(
        val company_id: Int,
        val town_id: Int
    )

    data class NearestAgentsBody(
        val location: Coordinates,
        val radiusInMeter: Int
    )

    data class Coordinates(
        val longitude: Double,
        val latitude: Double
    )

    data class BodyRequest(
        val app_id: Int,
        val company_id: Int,
        val text: String,
        val short_code: String,
        val amount: Int,
        val stk_number: String
    )

    data class AgentSearchPayload(val agent_name: String, val agent_phone: String)
    data class VillageAgentsPayload(val company_id: Int, val village_id: Int, val page: Int)
    data class TownAgentsPayload(val company_id: Int, val town_id: Int)
    data class MarkersPayload(val county: String, val longitude: Double, val latitude: Double)
    data class PreviousRARequestBody(val company_id: Int, val phone: String)

    data class CouponRequestBody(
        var coupon: String,
        var partner_id: Long,
        var products: List<CouponProduct>,
        var category_ids: List<Int>,
        var company_id: Int
    )

    data class CouponProduct(
        var id: Int,
        var quantity: Int
    )
    data class ValidateQuantityBody(
        val product_ids: List<String>
    )
}
