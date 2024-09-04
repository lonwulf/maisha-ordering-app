package com.lonwulf.core.objectboxdb.entity

import com.lonwulf.core.objectboxdb.BoxStoreObject
import io.objectbox.annotation.ConflictStrategy
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import java.util.Date

@Entity
data class PromoEntityOB(
    @Id(assignable = true)
    override var objectBoxID: Long = 0,
    val active: Boolean? = null,
    val endDate: String? = null,
    val endDateAsDate: Date? = Date(),
    @Unique(onConflict = ConflictStrategy.REPLACE)
    val id: Int? = null,
    val normalPrice: Double? = null,
    val priority: Int? = null,
    val productId: Long? = null,
    val promoPrice: Double? = null,
    val savings: Double? = null,
    val startDate: String? = null,
    val hidePromoPrice: Boolean? = false,
    val maxOrderQty: Int? = null
) : BoxStoreObject
