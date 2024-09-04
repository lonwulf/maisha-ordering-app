package com.lonwulf.cart.data.mapper

interface EntityMapper<Entity, Model> {
    fun mapToEntity(model: Model): Entity
}
