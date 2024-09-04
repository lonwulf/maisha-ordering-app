package com.lonwulf.cart.domain

import com.lonwulf.core.domain.models.Product

interface CartActionsInterface {
    fun editQuantity(
        position: Int,
        product: Product,
        quantity: Int
    )

    fun deleteCartItem(
        position: Int,
        product: Product
    )
}
