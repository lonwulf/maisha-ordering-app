package com.lonwulf.core.objectboxdb.entity

import com.lonwulf.core.objectboxdb.BoxStoreObject
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class HomePageEntityOB(
    @Id(assignable = true)
    override var objectBoxID: Long = 0,
    var sectionId: Int,
    var viewType: String = String(),
    var sectionTitle: String = String(),
    var priority: Int,
    var active: Boolean = false
) : BoxStoreObject
