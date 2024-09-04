package com.lonwulf.core.domain.repository

import com.lonwulf.core.domain.models.Category
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.objectboxdb.entity.CategoryEntityOB
import com.lonwulf.core.objectboxdb.entity.HomePageEntityOB
import com.lonwulf.core.objectboxdb.entity.PromoEntityOB
import com.lonwulf.core.objectboxdb.entity.SectionDataEntityOB
import com.lonwulf.core.utils.Result

interface DataRepository {
    suspend fun fetchAndInsertPromos(channel: String): Result<List<PromoEntityOB>>

    suspend fun fetchAndInsertProducts(): Result<Boolean>

    suspend fun fetchDeactivatedProducts(): Result<Long>

    suspend fun fetchProductStockQuantity(): Result<Boolean>

    suspend fun fetchHomePage(): Result<Map<HomePageEntityOB, List<SectionDataEntityOB>>>

    suspend fun findAllSectionsAndSectionDataItems(): Map<HomePageEntityOB, List<SectionDataEntityOB>>

    suspend fun getSectionProductsByIds(sections: List<SectionDataEntityOB>): List<Product>

    suspend fun fetchCategories(): Result<List<CategoryEntityOB>>

    suspend fun getParentCategories(): List<Category>

    suspend fun getSubCategoryByParentId(categoryId: Int): List<Category>

    suspend fun fetchCustomerStatus(): Result<Boolean>

}
