package com.lonwulf.category.domain.repositories

import androidx.paging.PagingSource
import com.lonwulf.core.domain.models.Category
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.objectboxdb.entity.ProductEntityOB
import com.lonwulf.core.objectboxdb.entity.PromoEntityOB
import com.lonwulf.core.objectboxdb.entity.SectionDataEntityOB

interface CategoryRepository {

    /***************** Local Database *****************/
    /**
     * Current implementation
     * Fetch all categories in db
     *
     * @return list of categories
     */
    suspend fun findAllCategories(): List<Category>

    suspend fun deleteCategories()

    suspend fun getParentCategories(): List<Category>

    suspend fun getSubCategoryByParentId(parentId: Int): List<Category>

    /**
     * Fetch all products from db
     *
     * @return list of categories
     */
    suspend fun findAllProducts(): List<Product>

    suspend fun getAllTmplProducts(tmplId: Int): List<Product>

    fun findAllPagedProducts(): PagingSource<Int, ProductEntityOB>

    fun findAllProductEntity(): List<ProductEntityOB>

    suspend fun getProductsByCategoryId(categoryId: Int): List<Product>

    fun getPagedProductsByCategoryId(categoryId: Int): PagingSource<Int, ProductEntityOB>

    fun getPagedProductsByCategoryIdSortedByPrice(
        categoryId: Int,
        isAscending: Int
    ): PagingSource<Int, ProductEntityOB>

    fun getProductsByCategoryIdSortedByPrice(
        categoryId: Int,
        isAscending: Int
    ): List<Product>


    fun getPagedSearchProductResult(searchItem: String): PagingSource<Int, ProductEntityOB>

    suspend fun getProductByCode(productCode: String): Product?
    suspend fun updateProductFavourite(product: Product): Product
    suspend fun insertItemToCart(item: Product): Long
    suspend fun updateItemQuantity(productCode: String, quantity: Int): Int

    suspend fun searchForProducts(search: String): List<Product>

    suspend fun getCartCount(): Int

    suspend fun getRecommendedProductsByCategoryId(categoryId: IntArray): Collection<Product>

    fun getPagedRecommendProductsByCategoryId(categoryId: IntArray): PagingSource<Int, ProductEntityOB>

    suspend fun updateProductsListing(isJNC: Boolean, productIdsListArray: MutableList<Int>)

    fun getSectionDataByIdPaged(sectionDataId: Int): PagingSource<Int, SectionDataEntityOB>
    suspend fun getProductsBySectionDataId(sectionDataId: Int): List<Product>
    fun getPromoByProductId(productId: Long?): PromoEntityOB?

    suspend fun getSectionProductsByIds(productIds: List<Int>): List<Product>

    fun getPagedProductsByCode(codes: ArrayList<String>): PagingSource<Int, ProductEntityOB>
    fun getProductsByCode(codes: ArrayList<String>): List<Product>



}
