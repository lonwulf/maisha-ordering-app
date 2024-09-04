@file:Suppress("PackageNaming")

package com.lonwulf.cart.data.mapper

import com.lonwulf.core.domain.models.Agent
import com.lonwulf.core.domain.models.StockInfo
import com.lonwulf.cart.data.CartService
import com.lonwulf.cart.data.response.CouponDTO
import com.lonwulf.cart.data.response.NearestAgentDTO
import com.lonwulf.cart.data.response.ValidateQuantityDto
import com.lonwulf.cart.domain.model.CouponModel
import com.lonwulf.cart.domain.model.CouponProductDataModel
import com.lonwulf.cart.domain.model.GeoJson
import com.lonwulf.cart.domain.model.NearestAgentModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

fun CouponDTO.mapToCouponModel(): CouponModel =
    CouponModel(
        is_valid = this.data?.is_valid,
        message = this.data?.message,
        discount = this.data?.discount,
        coupon_id = this.data?.coupon_id,
        minimum_amount = this.data?.minimum_amount,
        productIds = this.data?.product_ids,
        categoryIds = this.data?.category_ids
    )

fun List<CouponProductDataModel>.mapToCouponProductDTO(): List<CartService.CouponProduct> {
    val list = mutableListOf<CartService.CouponProduct>()
    this.map {
        it.let { model ->
            list.add(CartService.CouponProduct(id = model.id ?: 0, quantity = model.quantity ?: 0))
        }
    }
    return list
}





