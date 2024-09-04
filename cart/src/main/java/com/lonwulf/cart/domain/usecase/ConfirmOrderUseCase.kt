package com.lonwulf.cart.domain.usecase

import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Prefs
import com.lonwulf.core.utils.Result
import com.lonwulf.cart.data.response.ProcessOrderDto
import com.lonwulf.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConfirmOrderUseCase @Inject constructor(private val repository: CartRepository) {

    operator fun invoke(sms: String, amount: Double): Flow<ProcessOrderDto?> =
        flow {
            val response = repository.confirmOrder(
                Prefs.getLong(Constant.PrefKeys.PARTNER_ID),
                sms,
                Prefs.getString(Constant.PrefKeys.USER_TYPE) ?: "",
                amount,
                Prefs.getString(Constant.PrefKeys.PHONE_TO_PAY) ?: ""
            )
            var dataResponse: ProcessOrderDto? = null
            when (response) {
                is Result.Success -> {
                    dataResponse = response.data
                }

                is Result.Error -> {
                    dataResponse = ProcessOrderDto(null, "Error", 400)
                }

                else -> {}
            }
            emit(dataResponse)
        }
}
