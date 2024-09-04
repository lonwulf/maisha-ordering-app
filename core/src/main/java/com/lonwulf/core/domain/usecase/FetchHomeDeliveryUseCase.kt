package com.lonwulf.core.domain.usecase

import com.lonwulf.core.domain.repository.DataRepository
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Prefs
import com.lonwulf.core.utils.Result
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchHomeDeliveryUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(): Flow<List<HomeDelivery>> = flow {
        when (val homeDeliveryList = repository.fetchHomeDeliveryProducts()) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                val homeDeliveries = homeDeliveryList.data.map {
                    HomeDelivery(
                        code = it.code,
                        name = it.name,
                        description = it.description,
                        price = it.price
                    )
                }
                val gson = Gson()
                val json = gson.toJson(homeDeliveries)
                Prefs.putString(Constant.PrefKeys.HOME_DELIVERY_LIST, json)
                emit(homeDeliveries)
            }

            is Result.Error -> {
                emit(emptyList<HomeDelivery>())
            }
        }
    }
}
