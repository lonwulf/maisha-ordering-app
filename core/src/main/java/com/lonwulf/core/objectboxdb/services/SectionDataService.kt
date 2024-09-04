package com.lonwulf.core.objectboxdb.services

import android.os.Handler
import android.os.Looper
import androidx.paging.PagingSource
import com.lonwulf.core.objectboxdb.entity.SectionDataEntityOB
import com.lonwulf.core.objectboxdb.entity.SectionDataEntityOB_
import com.lonwulf.core.objectboxdb.store
import io.objectbox.android.ObjectBoxDataSource
import javax.inject.Inject

class SectionDataService @Inject constructor(private val boxService: BoxStoreService) {

    private val sectionDataBox = boxService.getBoxStoreBoxModel(SectionDataEntityOB::class.java)

    fun findAll(): List<SectionDataEntityOB> {
        return sectionDataBox.all
    }

    @Suppress("SwallowedException")
    fun insertAll(sectionData: List<SectionDataEntityOB>) {
        try {
            boxService.store(sectionData)
        } catch (e: ConcurrentModificationException) {
            insertWithDelay(sectionData)
        }
    }

    @Suppress("MagicNumber", "PrintStackTrace")
    private fun insertWithDelay(sectionData: List<SectionDataEntityOB>) {
        try {
            Handler(Looper.getMainLooper()).postDelayed({ boxService.store(sectionData) }, 500)
        } catch (e: ConcurrentModificationException) {
            e.printStackTrace()
        }
    }

    fun getSectionDataBySectionId(sectionId: Int): List<SectionDataEntityOB> {
        return sectionDataBox
            .query(SectionDataEntityOB_.sectionId.equal(sectionId))
            .build().use { it.find() }
    }

    fun getSectionDataByIdPaged(sectionDataId: Int): PagingSource<Int, SectionDataEntityOB> {
        return ObjectBoxDataSource.Factory(
            sectionDataBox
                .query(SectionDataEntityOB_.sectionDataId.equal(sectionDataId))
                .build()
        ).asPagingSourceFactory().invoke()
    }

    fun getSectionDataById(id: Int): SectionDataEntityOB? {
        return sectionDataBox
            .query(SectionDataEntityOB_.sectionDataId.equal(id))
            .build().use { it.find() }.firstOrNull()
    }

//    fun getSectionForPromosPricingByProductId(productId: String) : SectionDataEntityOB? {
//        return sectionDataBox
//                .query(SectionDataEntityOB_.promos
//                        .contains(String.format(Locale.ENGLISH,"\"product_id\":%s", productId)))
//                .build().findFirst()
//    }

    fun deleteAll() {
        sectionDataBox.removeAll()
    }

    fun delete(ids: List<Int>) {
        sectionDataBox.remove(
            sectionDataBox.query((SectionDataEntityOB_.sectionDataId.oneOf(ids.toIntArray())))
                .build().use { it.find() }
        )
    }
}
