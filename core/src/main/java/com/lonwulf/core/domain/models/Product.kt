package com.lonwulf.core.domain.models

import android.os.Parcelable
import com.lonwulf.core.database.entity.Attributes
import com.lonwulf.core.database.entity.Image
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Prefs
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Product(
    val id: Long = 0,
    val categoryId: Int = 0,
    val productTmplId: Int = 0,
    val attributes: @RawValue List<Attributes>? = mutableListOf(),
    val active: Boolean = false,
    val featured: Boolean = false,
    val code: String = "",
    val name: String = "",
    val description: String = "",
    val imageDate: Double = 0.0,
    var price: Double = 0.0,
    val priority: Int = 0,
    val productTemplateId: Int = 0,
    val canBeSold: Boolean = false,
    val imageUrl: String? = null,
    var oldPrice: Double = 0.0,
    var savings: Double = 0.0,
    var quantity: Int = 1,
    var favorite: Boolean = false,
    var isProductJnc: Boolean = false,
    val images: @RawValue Image? = null,
    var maxSaleQty: Int = 0,
    var minSaleQty: Int = 0,
    var slaDays: Int = 0,
    var slaQuantity: Int = 0,
    var saleDelay: Double = 0.0,
    var qoh: Int = 0,
    var rejectOrders: Boolean = false,
    var trackStock: Boolean = false,
    var hidePromoPrice: Boolean = false,
    var maxOrderQty: Int? = null,
    var promote: Boolean = false,
    var topSeller: Boolean = false,
    var clearance: Boolean = false,
    var appOnly: Boolean = false,
    var searchTerms: String? = null,
    var allDesc: String? = null,
    var longDesc: String? = null

) : Parcelable {
    fun getCurrencySymbol(): String {
        return Prefs.getString(Constant.MultiCompanyKeys.COUNTRY_CURRENCY_NAME).plus(" ")
    }

    fun hasSavings(): Boolean {
        return this.savings > 0
    }

    fun hidePromoPrice(): Boolean {
        return this.hidePromoPrice
    }

    fun hasSavingsAndShowPromoPrice(): Boolean {
        return hasSavings() && !hidePromoPrice()
    }

    fun hasSavingsAndHidePromoPrice(): Boolean {
        return hasSavings() && hidePromoPrice()
    }

    fun isProductJncEnabled(): Boolean {
        return this.isProductJnc &&
            Prefs.getString(Constant.PrefKeys.PARTNER_TYPE) == Constant.UserType.AGENT
    }

    fun hasOldPrice(): Boolean {
        return this.oldPrice > 0.0
    }

    fun isOutOfStock(): Boolean {
        return (this.qoh <= 0 && this.trackStock && this.rejectOrders)
    }
}
