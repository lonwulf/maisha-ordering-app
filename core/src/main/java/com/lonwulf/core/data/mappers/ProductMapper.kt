package com.lonwulf.core.data.mappers

import com.lonwulf.core.data.response.catalog.ProductsResponseDto
import com.lonwulf.core.database.entity.Attributes
import com.lonwulf.core.database.entity.Image
import com.lonwulf.core.objectboxdb.entity.ProductEntityOB
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Prefs
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("ComplexMethod")
fun ProductsResponseDto.mapApiResponseToProductEntity(): List<ProductEntityOB> {
    val dateFormat = SimpleDateFormat(Constant.LAST_SYNC_DATE_FORMAT_PATTERN, Locale.getDefault())
    this.data?.updatedTime?.takeIf { it.isEmpty().not() }?.let {
        dateFormat.parse(it)?.let { date ->
            Prefs.putDouble(Constant.PrefKeys.LAST_SYNC_TIME_STAMP, date.time.toDouble())
        }
    }
    val products = mutableListOf<ProductEntityOB>()
    this.data?.products?.map { productDetail ->
        // If partner is a customer, use price, if not, use price list
        val price =
            if (Prefs.getString(Constant.PrefKeys.PARTNER_TYPE) != Constant.UserType.CUSTOMER) {
                // check if priceList is zero and use the price instead
                if ((productDetail.pricelist_price ?: 0.0) > 0.0) {
                    productDetail.pricelist_price ?: 0.0
                } else {
                    productDetail.price ?: 0.0
                }
            } else {
                productDetail.price ?: 0.0
            }

        products.add(
            ProductEntityOB(
                categoryId = productDetail.categ_id ?: 0,
                attributes = setProductAttributes(productDetail.attributes),
                active = productDetail.active ?: false,
                featured = productDetail.featured ?: false,
                code = productDetail.code ?: "",
                name = productDetail.name ?: "",
                description = productDetail.description ?: "",
                imageDate = productDetail.image_date ?: 0.0,
                price = price,
                priority = productDetail.priority ?: 0,
                tempId = productDetail.product_tmpl_id ?: 0,
                canBeSold = productDetail.sale_ok ?: false,
                image = setProductImage(productDetail.images ?: mutableListOf()),
                isFavourite = false,
                isProductJNC = false,
                id = productDetail.id ?: 0,
                maxSaleQty = productDetail.max_sale_qty ?: 0,
                minSaleQty = productDetail.min_sale_qty ?: 0,
                slaDays = productDetail.sla_days,
                slaQuantity = productDetail.sla_quantity,
                saleDelay = productDetail.sale_delay,
                isCustomerOnly = productDetail.cust_only ?: false,
                searchTerms = productDetail.extensions?.search_terms,
                promote = productDetail.extensions?.promote ?: false,
                topSeller = productDetail.extensions?.top ?: false,
                clearance = productDetail.extensions?.clearance ?: false,
                appOnly = productDetail.extensions?.app_only ?: false,
            )
        )
    }
    return products
}

private fun setProductAttributes(attributes: List<Attributes>?): List<Attributes> {
    val attr: ArrayList<Attributes> = ArrayList()
    if (attributes != null) {
        if (attributes.isNotEmpty()) {
            attributes.forEachIndexed { _, attribute ->
                attr.add(Attributes(attribute.name, attribute.type, attribute.value))
            }
        }
    }
    return attr
}

private fun setProductImage(imagesList: List<ProductsResponseDto.Data.Products.Images>): Image? {
    val image: Image? = null
    if (imagesList.isNotEmpty()) {
        val foundImage = imagesList.find { it.position == 100 }
        return if (foundImage != null) {
            Image(foundImage.position, foundImage.url)
        } else {
            Image(imagesList.last().position, imagesList.last().url)
        }
    }
    return image
}
