@file:Suppress("PackageNaming")

package com.lonwulf.cart.domain.usecase

import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.core.utils.Result
import com.lonwulf.cart.domain.model.CouponModel
import com.lonwulf.cart.domain.model.CouponProductDataModel
import com.lonwulf.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ValidateCouponUseCase @Inject constructor(private val repository: CartRepository) {
    operator fun invoke(
        coupon: String,
        partnerId: Long,
        products: List<CouponProductDataModel>,
        categoryIds: ArrayList<Int>
    ): Flow<GenericApiResult<CouponModel?>> = flow {
        when (val result = repository.validateCoupons(coupon, partnerId, products, categoryIds)) {
            is Result.Loading -> Result.Loading
            is Result.Success -> emit(
                GenericApiResult(
                    results = result.data,
                    success = true,
                    message = "success"
                )
            )

            is Result.Error -> emit(
                GenericApiResult(
                    results = null,
                    success = false,
                    message = result.errorMessage ?: "something went wrong"
                )
            )
        }
    }
}
