package com.lonwulf.home.data.source

import com.lonwulf.core.network.RemoteDataSource
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Prefs
import com.lonwulf.core.utils.Result
import com.lonwulf.home.data.HomePageService
import com.lonwulf.home.data.UpdateLocationBody
import com.lonwulf.home.data.response.LocationDto
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineDispatcher

@Suppress("TooManyFunctions")
class HomepageRemoteDataSource(private val homePageService: HomePageService) : RemoteDataSource() {
    suspend fun updateCustomerLocation(
        dispatcher: CoroutineDispatcher,
        coordinates: LatLng
    ): Result<LocationDto> {
        return safeApiCall(dispatcher) {
            homePageService.updateCustomerLocation(
                UpdateLocationBody(
                    Prefs.getInt(Constant.PrefKeys.PARTNER_ID),
                    coordinates.latitude,
                    coordinates.longitude
                )
            )
        }
    }
}
