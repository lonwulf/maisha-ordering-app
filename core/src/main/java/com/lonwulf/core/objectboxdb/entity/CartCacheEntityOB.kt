@file:Suppress("ConstructorParameterNaming")

package com.lonwulf.core.objectboxdb.entity

import com.lonwulf.core.objectboxdb.BoxStoreObject
import io.objectbox.annotation.ConflictStrategy
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
data class CartCacheEntityOB(
    @Id(assignable = true)
    override var objectBoxID: Long = 0,
    @Unique(onConflict = ConflictStrategy.REPLACE)
    var item_code: String = String(),
    var image_url: String = String(),
    var item_name: String = String(),
    var item_price: Double = 0.0,
    var item_old_price: Double = 0.0,
    var item_saving: Double = 0.0,
    var item_favorite: Boolean = false,
    var item_quantity: Int = 0,
    var isProductJNC: Boolean = false,
    var maxSaleQty: Int = 0,
    var minSaleQty: Int = 0,
    var productId: Int = 0,
    var categoryId: Int = 0,
    var saleDelay: Double = 0.0,
    var slaDays: Int = 0,
    var slaQuantity: Int = 0,
    var qoh: Int = 0,
    var rejectOrders: Boolean = false,
    var trackStock: Boolean = false
) : BoxStoreObject
