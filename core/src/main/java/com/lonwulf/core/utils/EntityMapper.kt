package com.lonwulf.core.utils

interface EntityMapper<Entity, Model> {
    fun mapFromEntity(entity: Entity): Model
    fun mapToEntity(model: Model): Entity
}
