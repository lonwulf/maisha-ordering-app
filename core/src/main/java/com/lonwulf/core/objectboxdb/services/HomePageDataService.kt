package com.lonwulf.core.objectboxdb.services

import com.lonwulf.core.objectboxdb.entity.HomePageEntityOB
import com.lonwulf.core.objectboxdb.entity.HomePageEntityOB_
import com.lonwulf.core.objectboxdb.store
import javax.inject.Inject

class HomePageDataService @Inject constructor(private val boxService: BoxStoreService) {

    private val homePageDataBox = boxService.getBoxStoreBoxModel(HomePageEntityOB::class.java)

    fun findAll(): List<HomePageEntityOB> {
        return homePageDataBox
            .query(HomePageEntityOB_.active.equal(true))
            .order(HomePageEntityOB_.priority)
            .build().use { it.find() }
    }

    fun insertAll(order: List<HomePageEntityOB>) {
        boxService.store(order)
    }

    fun deleteAll() {
        homePageDataBox.removeAll()
    }
}
