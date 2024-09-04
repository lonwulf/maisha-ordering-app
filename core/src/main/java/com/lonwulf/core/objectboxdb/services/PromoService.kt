package com.lonwulf.core.objectboxdb.services

import com.lonwulf.core.objectboxdb.entity.PromoEntityOB
import com.lonwulf.core.objectboxdb.entity.PromoEntityOB_
import com.lonwulf.core.objectboxdb.store
import io.objectbox.kotlin.equal
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class PromoService @Inject constructor(private val boxService: BoxStoreService) {

    private val promoBox = boxService.getBoxStoreBoxModel(PromoEntityOB::class.java)

    fun insertAll(promos: List<PromoEntityOB>) {
        boxService.store(promos)
    }

    fun deleteAll() {
        promoBox.removeAll()
    }

    fun getPromoForProductId(productId: Long?): PromoEntityOB? {
        return productId?.let {
            promoBox
                .query(PromoEntityOB_.productId.equal(it))
                .greaterOrEqual(PromoEntityOB_.endDateAsDate, todayDate())
                .build().findFirst()
        }
    }

    private fun todayDate(): Date {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun findAll(): List<PromoEntityOB> {
        return promoBox
            .query((PromoEntityOB_.active equal 1))
            .greaterOrEqual(PromoEntityOB_.endDateAsDate, todayDate())
            .build().use { it.find() }
    }
}
