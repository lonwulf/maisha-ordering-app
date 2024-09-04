package com.lonwulf.category.domain.entities

data class AttributeData(
    var name: String?,
    var type: String?,
    var value: String?,
    var enabled: Boolean = false
)
