package com.lonwulf.core.objectboxdb.entity

import com.lonwulf.core.objectboxdb.BoxStoreObject
import io.objectbox.annotation.ConflictStrategy
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
data class StockEntityOB(
    @Id(assignable = true)
    override var objectBoxID: Long = 0,
    @Unique(onConflict = ConflictStrategy.REPLACE)
    var productId: Int,
    var qoh: Int,
    var rejectOrders: Boolean,
    var trackStock: Boolean
) : BoxStoreObject
