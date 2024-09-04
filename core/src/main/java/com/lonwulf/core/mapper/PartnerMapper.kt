package com.lonwulf.core.mapper

import com.lonwulf.core.data.response.partner.PartnerDetailsDto
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Prefs

object PartnerMapper {
    fun savePartnerData(partnerDataResult: PartnerDetailsDto?) {
        partnerDataResult?.data?.let { data ->
            // Save to prefs
            Prefs.putString(Constant.PrefKeys.PARTNER_NAME, data.name)
            Prefs.putString(Constant.PrefKeys.PARTNER_PHONE_NUMBER, data.phone)
            Prefs.putLong(Constant.PrefKeys.PARTNER_ID, data.id ?: 0L)
            Prefs.putString(Constant.PrefKeys.PARTNER_TYPE, data.partner_type)
            Prefs.putString(Constant.PrefKeys.PARTNER_LOCATION, data.location)
            Prefs.putDouble(Constant.PrefKeys.LATITUDE, data.latitude ?: 0.0)
            Prefs.putDouble(Constant.PrefKeys.LONGITUDE, data.longitude ?: 0.0)
            Prefs.putString(Constant.PrefKeys.PRICE_LIST, data.price_list)
            Prefs.putLong(Constant.PrefKeys.PRICE_LIST_ID, data.price_list_id ?: 0L)
            Prefs.putString(Constant.PrefKeys.WAREHOUSE, data.warehouse)
            Prefs.putString(Constant.PrefKeys.DELIVERY_CLASS, data.delivery_class)
            Prefs.putDouble(Constant.PrefKeys.CUSTOMER_LIMIT, data.customer_limit ?: -1.0)
            Prefs.putString(Constant.PrefKeys.PARTNER_FIRST_NAME, data.first_name)
            Prefs.putString(Constant.PrefKeys.PARTNER_LAST_NAME, data.last_name)
        }
    }
}
