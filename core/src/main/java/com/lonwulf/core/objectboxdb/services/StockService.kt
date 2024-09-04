package com.lonwulf.core.objectboxdb.services

import com.lonwulf.core.objectboxdb.entity.StockEntityOB
import com.lonwulf.core.objectboxdb.entity.StockEntityOB_
import com.lonwulf.core.objectboxdb.store
import io.objectbox.kotlin.equal
import javax.inject.Inject

class StockService @Inject constructor(private val boxStoreService: BoxStoreService) {
    private val stockBox = boxStoreService.getBoxStoreBoxModel(StockEntityOB::class.java)

    fun insert(stockEntityOB: StockEntityOB): Long {
        return boxStoreService.store(stockEntityOB)
    }

    fun insertAll(stockData: List<StockEntityOB>) {
        boxStoreService.store(stockData)
    }

    fun findAll(): List<StockEntityOB> = stockBox.all

    fun getProductStockInfo(productId: Int): StockEntityOB? {
        return stockBox
            .query(StockEntityOB_.productId equal productId)
            .build().use { it.findUnique() }
    }

    fun deleteAll() {
        stockBox.removeAll()
    }
}
