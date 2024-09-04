@file:Suppress("ConstructorParameterNaming", "FunctionParameterNaming", "TooManyFunctions")

package com.lonwulf.home.data

import com.lonwulf.home.data.response.LocationDto
import retrofit2.http.Body
import retrofit2.http.POST

interface HomePageService {
    @POST("cms/v1/partners/update_location")
    suspend fun updateCustomerLocation(@Body updateLocationBody: UpdateLocationBody): LocationDto
}

data class UpdateLocationBody(val partner_id: Int, val latitude: Double, val longitude: Double)
