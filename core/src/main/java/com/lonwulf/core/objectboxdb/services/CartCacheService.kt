package com.lonwulf.core.objectboxdb.services

import android.util.Log
import com.lonwulf.core.objectboxdb.entity.CartCacheEntityOB
import com.lonwulf.core.objectboxdb.entity.CartCacheEntityOB_
import com.lonwulf.core.objectboxdb.store

class CartCacheService(private val boxService: BoxStoreService) {

    private val cartBox = boxService.getBoxStoreBoxModel(CartCacheEntityOB::class.java)

    fun insertItemToCart(product: CartCacheEntityOB): Long {
        return boxService.store(product)
    }

    fun deleteItemFromCart(code: String): Int {
        val cartEntities = cartBox.query(CartCacheEntityOB_.item_code.equal(code))
            .build().use { it.find() }
        cartBox.remove(cartEntities)
        return cartEntities.size
    }

    fun deleteAllItemsInCart() {
        cartBox.removeAll()
    }

    fun updateCartItem(productCode: String, quantity: Int): Int {
        val cartEntities = cartBox.query(CartCacheEntityOB_.item_code.equal(productCode))
            .build().use { it.find() }
        cartEntities.forEach {
            Log.e("fee ccs:", "qty $quantity")
            it.item_quantity = quantity
            insertItemToCart(it)
        }
        return cartEntities.size
    }

    fun getCartItems(): List<CartCacheEntityOB> {
        return cartBox.all
    }

    fun getNumberOfCartItems(): Int {
        return cartBox.count().toInt()
    }

    suspend fun deleteNonJNCProducts(productArray: Array<CartCacheEntityOB>) {
        cartBox.remove(productArray.asList().filter { it.objectBoxID != 0L })
    }

    suspend fun getItemQuantity(productCode: String): Int? {
        return cartBox.query(CartCacheEntityOB_.item_code.equal(productCode))
            .build().property(CartCacheEntityOB_.item_quantity).findInt()
    }
}
