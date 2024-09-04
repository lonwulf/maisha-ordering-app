@file:Suppress("ConstructorParameterNaming")

package com.lonwulf.core.data

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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface CoreService {

    @POST("cms/v2/partners/partner_details")
    suspend fun fetchPartnerDetails(@Body bodyRequest: BodyRequest): PartnerDetailsDto


    @GET
    suspend fun fetchPromosFromConfig(@Url urlString: String): PromoResponseDto

    @POST("cms/v2/products/promos")
    suspend fun fetchChannelSpecificPromos(@Body bodyRequest: PromoRequestBody): PromoResponseDto


    @GET
    suspend fun fetchProducts(@Url urlString: String): ProductsResponseDto


    @POST("cms/v2/products/inactive")
    suspend fun fetchDeactivatedProducts(@Body bodyRequest: DeactivatedProductsRequestBody): DeactivatedProductsDto

    @GET
    suspend fun fetchProductQuantity(@Url productQtyUrl: String): ProductQuantityResponseDto

    @POST("cms/v2/ecat/homepage")
    suspend fun fetchHomePage(@Body bodyRequest: HomePageRequestBody): HomePageResponseDto

    @POST("cms/v2/products/categories")
    suspend fun fetchCategories(@Body bodyRequest: MissingCompanyIdRequestBody): CategoriesResponseDto

    @GET("cms/v1/partners/customer_status/{customer_id} ")
    suspend fun fetchCustomerStatus(@Path("customer_id") customerId: Long): CustomerStatusResponseDto
}

data class BodyRequest(
    val partner_id: Long,
)

data class FeeConfigBody(val agent_id: Int, val warehouse: String, val delivery_class: String)
