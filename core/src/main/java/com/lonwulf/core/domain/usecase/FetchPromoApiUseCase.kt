@file:Suppress("PackageNaming")

package com.lonwulf.core.domain.usecase

import com.lonwulf.core.domain.repository.DataRepository
import com.lonwulf.core.objectboxdb.entity.PromoEntityOB
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Constant.PrefKeys.IS_LOGGED_IN
import com.lonwulf.core.utils.Constant.PrefKeys.PARTNER_TYPE
import com.lonwulf.core.utils.Constant.UserType.AGENT
import com.lonwulf.core.utils.Constant.UserType.CUSTOMER
import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.core.utils.Prefs
import com.lonwulf.core.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchPromoApiUseCase @Inject constructor(private val repository: DataRepository) {
    private val isCustomerOrNotLoggedIn by lazy {
        Prefs.getString(PARTNER_TYPE).equals(CUSTOMER) || Prefs.getBoolean(IS_LOGGED_IN).not()
    }
    private val partnerIsAgent by lazy { Prefs.getString(PARTNER_TYPE).equals(AGENT) }
    private val channel by lazy {
        when {
            isCustomerOrNotLoggedIn -> Constant.PromoChannels.ECATALOG.getChannel()
            partnerIsAgent -> Constant.PromoChannels.A_ECAT.getChannel()
            else -> "" // trader not handled
        }
    }

    suspend operator fun invoke(): Flow<GenericApiResult<List<PromoEntityOB>>> = flow {
        when (val result = repository.fetchAndInsertPromos(channel)) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                val fetchInsertResult = result.data
                val apiResult = GenericApiResult(
                    results = fetchInsertResult,
                    success = true,
                    message = "successful"
                )
                emit(apiResult)
            }

            is Result.Error -> {
                val apiResult = GenericApiResult(
                    results = emptyList<PromoEntityOB>(),
                    success = false,
                    message = result.errorMessage ?: "something went wrong"
                )
                emit(apiResult)
            }
        }
    }
}
