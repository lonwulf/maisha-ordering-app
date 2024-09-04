package com.lonwulf.home.domain.repositories

import com.lonwulf.core.domain.models.Category
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.domain.models.SectionData
import com.lonwulf.core.objectboxdb.entity.HomePageEntityOB
import com.lonwulf.core.objectboxdb.entity.SectionDataEntityOB
import com.lonwulf.core.utils.Result
import com.lonwulf.home.data.response.LocationDto
import com.google.android.gms.maps.model.LatLng

interface HomePageRepository {

    suspend fun findAllSectionsAndSectionDataItems(): Map<HomePageEntityOB, List<SectionDataEntityOB>>

    suspend fun getSectionDataBySectionId(sectionId: Int): List<SectionData>

    suspend fun getSectionProductsByIds(sections: List<SectionDataEntityOB>): List<Product>

    suspend fun deleteHomePageUseCase()

    suspend fun getSubCategoryByParentId(categoryId: Int): List<Category>

    suspend fun getProductByCode(productCode: String): Product?

    suspend fun getProductsByCategoryId(categoryId: Int): Collection<Product>
    suspend fun getRecommendedProductsByCategoryId(categoryId: IntArray): Collection<Product>
    suspend fun getRecommendedProductsByBrandNames(brandNames: Array<String>): Collection<Product>
    suspend fun getProductsWithProductCodes(productCodesArray: Array<String>): Collection<Product>
    suspend fun getParentCategories(): List<Category>

    suspend fun insertItemToCart(item: Product): Long

    suspend fun updateProductFavourite(productCode: String?): Product?

    suspend fun getCartCount(): Int
    suspend fun clearHomePageData()

    suspend fun getSectionDataById(sectionId: Int): SectionData?

    suspend fun updateCustomerLocation(coordinates: LatLng): Result<LocationDto>

    suspend fun getSubCategoryById(subCategoryId: Int): Category?
}
