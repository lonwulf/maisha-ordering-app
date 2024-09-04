package com.lonwulf.core.mapper

import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.objectboxdb.entity.CartCacheEntityOB
import com.lonwulf.core.objectboxdb.entity.ProductEntityOB
import com.lonwulf.core.objectboxdb.entity.PromoEntityOB
import com.lonwulf.core.utils.Constant

fun ProductEntityOB.mapProductEntityToDomain(): Product {
    return Product(
        this.id.toLong(),
        this.categoryId,
        productTmplId = this.tempId,
        attributes = this.attributes,
        active = this.active,
        featured = this.featured,
        code = this.code,
        name = this.name,
        description = this.description,
        imageDate = this.imageDate,
        price = this.price,
        priority = this.priority,
        productTemplateId = this.tempId,
        canBeSold = this.canBeSold,
        imageUrl = this.image.let { it?.url?.mapImageUrl() },
        oldPrice = this.price,
        savings = 0.0,
        quantity = this.itemQty,
        favorite = this.isFavourite,
        isProductJnc = this.isProductJNC,
        minSaleQty = this.minSaleQty,
        maxSaleQty = this.maxSaleQty,
        images = null,
        saleDelay = this.saleDelay,
        slaDays = this.slaDays,
        slaQuantity = this.slaQuantity,
        qoh = this.qoh,
        rejectOrders = this.rejectOrders,
        trackStock = this.trackStock,
        searchTerms = this.searchTerms,
        promote = this.promote,
        topSeller = this.topSeller,
        clearance = this.clearance,
        appOnly = this.appOnly,
    )
}

fun CartCacheEntityOB.mapToProductEntityOB(): ProductEntityOB = ProductEntityOB(
    categoryId = this.categoryId,
    code = this.item_code,
    name = this.item_name,
    price = this.item_price,
    isFavourite = false,
    isProductJNC = this.isProductJNC,
    id = this.productId,
    maxSaleQty = this.maxSaleQty,
    minSaleQty = this.minSaleQty,
    itemQty = this.item_quantity,
    saleDelay = this.saleDelay,
    slaDays = this.slaDays,
    slaQuantity = this.slaQuantity,
    qoh = this.qoh,
    rejectOrders = this.rejectOrders,
    trackStock = this.trackStock
)

fun Product.mapProductDomainToEntity(): ProductEntityOB {
    return ProductEntityOB(
        categoryId = this.categoryId,
        attributes = this.attributes,
        active = this.active,
        featured = this.featured,
        code = this.code,
        name = this.name,
        description = this.description,
        imageDate = this.imageDate,
        price = this.price,
        priority = this.priority,
        tempId = this.productTemplateId,
        canBeSold = this.canBeSold,
        image = this.images,
        isFavourite = false,
        isProductJNC = this.isProductJnc,
        id = this.id.toInt(),
        maxSaleQty = this.maxSaleQty,
        minSaleQty = this.minSaleQty,
        itemQty = this.quantity,
        saleDelay = this.saleDelay,
        slaQuantity = this.slaQuantity,
        slaDays = this.slaDays,
        qoh = this.qoh,
        rejectOrders = this.rejectOrders,
        trackStock = this.trackStock,
        searchTerms = this.searchTerms,
        promote = this.promote,
        topSeller = this.topSeller,
        clearance = this.clearance,
        appOnly = this.appOnly,
    )
}

fun String.mapImageUrl(): String {
    val imageUrlHeader = Constant.getBaseImageUrl()
    return if (this.startsWith(imageUrlHeader)) this else Constant.getBaseImageUrl() + this
}

fun mapProductEntityToDomainWithPromos(
    input: List<ProductEntityOB>,
    promos: List<PromoEntityOB>
): List<Product> {
    return input.map { productDBObject ->
        productDBObject.mapProductEntityToDomain()
    }.map { product ->
        val promo = promos.find { promo -> promo.productId == product.id }
        promo?.let { promoEntityOB ->
            mapProductSavings(product, promoEntityOB)
        }
        product
    }
}

@Suppress("NestedBlockDepth")
fun mapProductSavings(
    product: Product,
    promo: PromoEntityOB?,
    forceToShowPromoPrices: Boolean = false
): Product {
    return product.apply {
        val promoPriceIsGreaterThanProductPrice =
            (promo?.savings ?: 0.0) > product.price
        promo?.let {
            product.oldPrice = promo.normalPrice ?: 0.0
            if (!promoPriceIsGreaterThanProductPrice) {
                product.price = if (forceToShowPromoPrices) {
                    promo.normalPrice?.let { it - (promo.savings ?: 0.0) } ?: product.price
                } else {
                    promo.promoPrice ?: 0.0
                }
            }
            product.savings = if (promoPriceIsGreaterThanProductPrice) 0.0 else promo.savings ?: 0.0
            product.hidePromoPrice = promo.hidePromoPrice ?: false
            promo.maxOrderQty?.let { product.maxOrderQty = it }
        }
    }
}
