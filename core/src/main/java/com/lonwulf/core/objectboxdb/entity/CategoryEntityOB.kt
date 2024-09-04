package com.lonwulf.core.objectboxdb.entity

import com.lonwulf.core.database.entity.Image
import com.lonwulf.core.database.entity.Video
import com.lonwulf.core.objectboxdb.BoxStoreObject
import com.lonwulf.core.objectboxdb.ConvertersOB
import io.objectbox.annotation.ConflictStrategy
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
data class CategoryEntityOB(
    @Id(assignable = true)
    override var objectBoxID: Long = 0,
    @Unique(onConflict = ConflictStrategy.REPLACE)
    var categoryId: Int = 0,
    var name: String = String(),
    var displayName: String = String(),
    var priority: Int? = null,
    var videoActive: Boolean = false,
    var featured: Boolean = false,
    var parentId: Int? = null,
    var color: String? = String(),
    @Convert(
        converter = ConvertersOB.ImageTypeConverterOB::class,
        dbType = String::class
    ) var image: Image? = null,
    @Convert(
        converter = ConvertersOB.ImageTypeConverterOB::class,
        dbType = String::class
    ) var icon: Image? = null,
    @Convert(
        converter = ConvertersOB.VideoTypeConverter::class,
        dbType = String::class
    ) var video: Video? = null
) : BoxStoreObject
