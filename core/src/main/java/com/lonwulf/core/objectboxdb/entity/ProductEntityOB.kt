package com.lonwulf.core.objectboxdb.entity

import com.lonwulf.core.database.entity.Attributes
import com.lonwulf.core.database.entity.Image
import com.lonwulf.core.objectboxdb.BoxStoreObject
import com.lonwulf.core.objectboxdb.ConvertersOB
import io.objectbox.annotation.ConflictStrategy
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import java.util.Date

@Entity
data class ProductEntityOB(
    @Id(assignable = true)
    override var objectBoxID: Long = 0,
    var id: Int,
    @Unique(onConflict = ConflictStrategy.REPLACE)
    var code: String = String(),
    var createdAt: Date = Date(System.currentTimeMillis()),
    var updatedAt: Date = Date(System.currentTimeMillis()),
    var categoryId: Int,
    @Convert(
        converter = ConvertersOB.AttributesConverter::class,
        dbType = String::class
    ) var attributes: List<Attributes>? = null,
    var active: Boolean = false,
    var featured: Boolean = false,
    var name: String = String(),
    var description: String = String(),
    var imageDate: Double = 0.0,
    var price: Double = 0.0,
    var priority: Int = 0,
    var tempId: Int = 0,
    var canBeSold: Boolean = false,
    @Convert(
        converter = ConvertersOB.ImageTypeConverterOB::class,
        dbType = String::class
    ) var image: Image? = null,
    var isFavourite: Boolean = false,
    var isProductJNC: Boolean = false,
    var maxSaleQty: Int = 0,
    var minSaleQty: Int = 0,
    var itemQty: Int = 1,
    var slaDays: Int = 0,
    var slaQuantity: Int = 0,
    var saleDelay: Double = 0.0,
    var isCustomerOnly: Boolean = false,
    var qoh: Int = 0,
    var rejectOrders: Boolean = false,
    var trackStock: Boolean = false,
    var promote: Boolean = false,
    var topSeller: Boolean = false,
    var clearance: Boolean = false,
    var appOnly: Boolean = false,
    var searchTerms: String? = null,
    var allDesc: String? = null,
    var longDesc: String? = null
) : BoxStoreObject
