package com.lonwulf.core.domain.models

import android.widget.ImageView

interface ProductClickListener {
    fun onProductItemClicked(product: Product, imageView: ImageView)
    fun onBuyNowClicked(product: Product)
    fun handleFavouritePress(product: Product)
}
