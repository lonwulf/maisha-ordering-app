package com.lonwulf.core.objectboxdb.entity

import com.lonwulf.core.objectboxdb.BoxStoreObject
import com.lonwulf.core.objectboxdb.ConvertersOB
import io.objectbox.annotation.ConflictStrategy
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import java.util.Date

@Entity
data class MessageCacheEntityOB(
    @Id(assignable = true)
    override var objectBoxID: Long = 0,
    @Unique(onConflict = ConflictStrategy.REPLACE)
    var id: Int,
    var title: String? = null,
    var message: String? = null,
    var body: String? = null,
    var senderId: String? = null,
    var productId: Int? = null,
    var promotionId: Int? = null,
    var url: String? = null,
    var isRead: Boolean? = false,
    var createdAt: Date? = null,
    @Convert(
        converter = ConvertersOB.ProductCodesConverter::class,
        dbType = String::class
    )
    var productCodes: List<String>? = ArrayList(),
    @Convert(
        converter = ConvertersOB.ProductCodesConverter::class,
        dbType = String::class
    )
    var categoryNames: List<String>? = ArrayList(),
    var isLinked: Boolean? = false,
    var sectionIdentifier: String? = null,
    var sectionId: Int? = null
) : BoxStoreObject
