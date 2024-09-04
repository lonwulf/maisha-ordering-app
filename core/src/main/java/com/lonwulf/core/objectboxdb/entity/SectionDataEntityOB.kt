package com.lonwulf.core.objectboxdb.entity

import com.lonwulf.core.database.BaseEntity
import com.lonwulf.core.objectboxdb.BoxStoreObject
import com.lonwulf.core.objectboxdb.ConvertersOB
import io.objectbox.annotation.ConflictStrategy
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
data class SectionDataEntityOB(
    @Id(assignable = true)
    override var objectBoxID: Long = 0,
    @Unique(onConflict = ConflictStrategy.REPLACE)
    var sectionId: Int = 0,
    var sectionDataId: Int = 0,
    var image: String = String(),
    var title: String = String(),
    @Convert(
        converter = ConvertersOB.ProductIdsConverter::class,
        dbType = String::class
    )
    var productIds: List<Int>? = arrayListOf(),
) : BoxStoreObject, BaseEntity()
