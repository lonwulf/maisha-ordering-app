package com.lonwulf.core.data.mappers

import com.lonwulf.core.data.response.catalog.PromoResponseDto
import com.lonwulf.core.objectboxdb.entity.ProductEntityOB
import com.lonwulf.core.objectboxdb.entity.PromoEntityOB
import com.lonwulf.core.utils.Utils
import java.util.Date

@Suppress("MagicNumber")
fun PromoResponseDto.Data?.mapApiResponseToPromoEntity(
    channel: String,
    allProducts: List<ProductEntityOB>
): List<PromoEntityOB> {
    val input = this
    val mappedPromos = mutableListOf<PromoEntityOB>()
    // map category discounts
    input?.category_discounts?.data?.filter { it.active == true }?.map { dto ->
        if (dto.channel_applicable?.contains(channel) == true) {
            allProducts.filter { it.categoryId == dto.product_category_id }
                .forEach { product ->
                    val savingsFromPromo = if (dto.type == "percentage") {
                        product.price?.times(dto.value?.div(100) ?: 0.0)
                    } else {
                        dto.value
                    }
                    mappedPromos.add(createPromoEntityOB(dto, product, savingsFromPromo))
                }
        }
    }
    // map product discounts
    input?.product_discounts?.data?.filter { it.active == true }?.map { dto ->
        if (dto.channel_applicable?.contains(channel) == true) {
            allProducts.find { it.id == dto.product_id?.toInt() }?.let { product ->
                val savingsFromPromo = if (dto.type == "percentage") {
                    product.price?.times(dto.value?.div(100) ?: 0.0)
                } else {
                    dto.value
                }
                mappedPromos.add(createPromoEntityOB(dto, product, savingsFromPromo))
            }
        }
    }
    // map from the older api
    input?.promos?.filter { it.active == true }?.map { dto ->
        allProducts.find { it.id == dto.product_id?.toInt() }?.let { product ->
            val savingsFromPromo = dto.savings ?: 0.0
            if (savingsFromPromo <= 0.0) return@map
            mappedPromos.add(createPromoEntityOB(dto, product, savingsFromPromo))
        }
    }
    return mappedPromos
}

private fun createPromoEntityOB(
    dto: PromoResponseDto.PromoData,
    product: ProductEntityOB,
    savingsFromPromo: Double?
) = PromoEntityOB(
    active = true,
    id = product.id,
    normalPrice = product.price,
    priority = 0,
    productId = product.id.toLong(),
    promoPrice = if (dto.hide_promo_price == true) {
        product.price
    } else {
        (
            product.price?.minus(
                savingsFromPromo ?: 0.0
            )
            )
    },
    savings = savingsFromPromo,
    startDate = dto.start_date,
    endDate = dto.end_date,
    hidePromoPrice = dto.hide_promo_price,
    endDateAsDate = Utils.formatDateStringToDate(
        dto.end_date,
        currentFormat = "yyyy-MM-dd'T'HH:mm:ss"
    ) ?: Date(),
    maxOrderQty = dto.max_order_qty ?: 0
)
