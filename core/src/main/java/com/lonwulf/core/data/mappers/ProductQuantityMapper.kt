package com.lonwulf.core.data.mappers

import com.lonwulf.core.data.response.catalog.ProductQuantityResponseDto
import com.lonwulf.core.domain.models.StockInfo
import com.lonwulf.core.objectboxdb.entity.StockEntityOB

fun ProductQuantityResponseDto.mapApiResponseToStockEntity(): List<StockEntityOB> {
    val stockInfo = mutableListOf<StockEntityOB>()
    this.data.products.filter { it.stock != null && it.product_id != null }
        .map { data ->
            data.stock?.qoh?.let {
                stockInfo.add(
                    StockEntityOB(
                        productId = data.product_id.toInt(),
                        qoh = if (it < 0.0) 0 else data.stock.qoh.toInt(),
                        rejectOrders = data.stock.reject_orders,
                        trackStock = data.stock.track_stock
                    )
                )
            }
        }
    return stockInfo
}

fun StockEntityOB.mapEntityToDomain(): StockInfo {
    return StockInfo(
        productId = this.productId,
        qoh = this.qoh,
        trackStock = this.trackStock,
        rejectOrders = this.rejectOrders
    )
}

fun ProductQuantityResponseDto.mapApiResponseToDomainModelList(): List<StockInfo> {
    val stockInfoList = mutableListOf<StockInfo>()
    this.data.products.filter { it.stock != null && it.product_id != null }
        .map { data ->
            data.stock?.qoh?.let {
                stockInfoList.add(
                    StockInfo(
                        productId = data.product_id.toInt(),
                        qoh = if (it < 0.0) 0 else data.stock.qoh.toInt(),
                        rejectOrders = data.stock.reject_orders,
                        trackStock = data.stock.track_stock
                    )
                )
            }
        }
    return stockInfoList
}
