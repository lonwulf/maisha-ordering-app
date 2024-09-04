@file:Suppress("ConstructorParameterNaming", "TooManyFunctions")

package com.lonwulf.cart.domain.repository

import com.lonwulf.cart.data.response.AgentMarkersDto
import com.lonwulf.cart.data.response.AgentSearchDto
import com.lonwulf.cart.data.response.NearestAgentDTO
import com.lonwulf.cart.data.response.ProcessOrderDto
import com.lonwulf.cart.data.response.ValidateQuantityDto
import com.lonwulf.cart.domain.model.CouponModel
import com.lonwulf.cart.domain.model.CouponProductDataModel

interface CartRepository {
    suspend fun confirmOrder(
        agentId: Long, sms: String, partnerType: String, amount: Double, phoneToPay: String
    ): Result<ProcessOrderDto>

    suspend fun agentSearch(
        agentName: String, agentPhone: String
    ): Result<AgentSearchDto>


    suspend fun agentOnMap(
        county: String, longitude: Double, latitude: Double
    ): Result<AgentMarkersDto>


    suspend fun validateCoupons(
        coupon: String,
        partnerId: Long,
        products: List<CouponProductDataModel>,
        categoryIds: ArrayList<Int>
    ): Result<CouponModel>

    suspend fun validateQuantity(productIds: List<String>): Result<ValidateQuantityDto>

    suspend fun fetchNearByAgents(
        latitude: Double, longitude: Double, radius: Int
    ): Result<NearestAgentDTO>
}
