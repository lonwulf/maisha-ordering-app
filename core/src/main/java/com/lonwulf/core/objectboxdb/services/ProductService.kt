package com.lonwulf.core.objectboxdb.services

import androidx.paging.PagingSource
import com.lonwulf.core.objectboxdb.entity.ProductEntityOB
import com.lonwulf.core.objectboxdb.entity.ProductEntityOB_
import com.lonwulf.core.objectboxdb.entity.StockEntityOB
import com.lonwulf.core.objectboxdb.store
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Constant.PrefKeys.PARTNER_TYPE
import com.lonwulf.core.utils.Constant.UserType.AGENT
import com.lonwulf.core.utils.Prefs
import io.objectbox.android.ObjectBoxDataSource
import io.objectbox.kotlin.equal
import io.objectbox.kotlin.or
import io.objectbox.query.Query
import io.objectbox.query.QueryBuilder
import javax.inject.Inject

class ProductService @Inject constructor(private val boxService: BoxStoreService) {
    private val partnerIsAgent by lazy { Prefs.getString(PARTNER_TYPE).equals(AGENT) }

    private val productBox = boxService.getBoxStoreBoxModel(ProductEntityOB::class.java)

    fun findAll(): List<ProductEntityOB> = canBeSoldAndActiveQuery().build().use { it.find() }

    fun insert(entityData: ProductEntityOB) = boxService.store(entityData)

    fun update(entityData: ProductEntityOB) = insert(entityData)

    fun insertAll(categories: List<ProductEntityOB>) = boxService.store(categories)

    fun deleteAll() = productBox.removeAll()

    fun findAllProductEntity(): List<ProductEntityOB> = findAll()

    fun getRecommendProductsByCategoryId(categoryIds: IntArray): List<ProductEntityOB> =
        categoryIdsProductsThatCanBeSoldAndActive(categoryIds).build().use { it.find() }

    fun getPagedProductsByCategoryId(catId: Int): PagingSource<Int, ProductEntityOB> =
        buildPagingSource(categoryProductsThatCanBeSoldAndActive(catId).build())

    fun getPagedRecommendProductsByCategoryId(categoryIds: IntArray): PagingSource<Int, ProductEntityOB> =
        buildPagingSource(categoryIdsProductsThatCanBeSoldAndActive(categoryIds).build())

    fun findProductsByProductIds(productIdsList: MutableList<Int>): List<ProductEntityOB> {
        return canBeSoldAndActiveQuery()
            .apply(ProductEntityOB_.id.oneOf(productIdsList.toIntArray()))
            .build()
            .use { it.find() }
    }

    fun getProductByCode(code: String): ProductEntityOB? {
        return canBeSoldAndActiveQuery()
            .apply(ProductEntityOB_.code equal code)
            .build()
            .use { it.findUnique() }
    }

    fun getProductsByCategoryId(catId: Int): List<ProductEntityOB> {
        return categoryProductsThatCanBeSoldAndActive(catId)
            .build()
            .use { it.find() }
    }

    fun getAllTmplProducts(tmplId: Int): List<ProductEntityOB> {
        return canBeSoldAndActiveQuery()
            .apply(ProductEntityOB_.tempId equal tmplId)
            .build().use { it.find() }
    }

    fun findAllPagedProducts(): PagingSource<Int, ProductEntityOB> {
        return buildPagingSource(
            canBeSoldAndActiveQuery()
                .order(ProductEntityOB_.priority)
                .build()
        )
    }

    fun getPagedProductsByCategoryIdSortedByPrice(
        catId: Int,
        isAscending: Int
    ): PagingSource<Int, ProductEntityOB> {
        val query = categoryProductsThatCanBeSoldAndActive(catId)
            .order(ProductEntityOB_.price, isAscending)
            .build()
        if (partnerIsAgent) {
            query.apply { ProductEntityOB_.isCustomerOnly equal 0 }
        }
        return buildPagingSource(query)
    }

    fun getProductsByCategoryIdSortedByPrice(
        catId: Int,
        isAscending: Int
    ): List<ProductEntityOB> {
        val query = categoryProductsThatCanBeSoldAndActive(catId)
            .order(ProductEntityOB_.price, isAscending)
            .build()
        if (partnerIsAgent) {
            query.apply { ProductEntityOB_.isCustomerOnly equal 0 }
        }
        return query.use { it.find() }
    }

    fun getRecommendProductsByBrands(brandNames: String): List<ProductEntityOB> {
        return canBeSoldAndActiveQuery()
            .apply(ProductEntityOB_.name.contains(brandNames))
            .build().use { it.find(Constant.Pagination.offset, Constant.Pagination.limit) }
    }

    fun getProductsWithCodes(codeArray: Array<String>): List<ProductEntityOB> {
        return canBeSoldAndActiveQuery()
            .apply(ProductEntityOB_.code.oneOf(codeArray))
            .build().use { it.find(Constant.Pagination.offset, Constant.Pagination.limit) }
    }

    fun getSearchProductResult(searchItem: String): List<ProductEntityOB> {
        return canBeSoldAndActiveQuery()
            .apply(
                ProductEntityOB_.name.contains(
                    searchItem,
                    QueryBuilder.StringOrder.CASE_INSENSITIVE
                )
            )
            .order(ProductEntityOB_.featured)
            .order(ProductEntityOB_.priority)
            .order(ProductEntityOB_.name)
            .build().use { it.find() }
    }

    fun getPagedSearchProductResult(searchItem: String): PagingSource<Int, ProductEntityOB> {
        return buildPagingSource(
            canBeSoldAndActiveQuery()
                .apply(
                    ProductEntityOB_.name.contains(
                        searchItem,
                        QueryBuilder.StringOrder.CASE_INSENSITIVE
                    )
                )
                .order(ProductEntityOB_.priority)
                .build()
        )
    }

    fun getSearchProductResultByPriority(searchItem: String): List<ProductEntityOB> {
        return canBeSoldAndActiveQuery()
            .apply(
                ProductEntityOB_.name.contains(
                    searchItem,
                    QueryBuilder.StringOrder.CASE_INSENSITIVE
                )
            )
            .order(ProductEntityOB_.priority)
            .build().use { it.find() }
    }

    fun getFavouriteProducts(isFavourite: Boolean): List<ProductEntityOB> {
        return productBox
            .query((ProductEntityOB_.isFavourite equal isFavourite))
            .build().use { it.find() }
    }

    fun getJNCProducts(isJNC: Boolean): List<ProductEntityOB> {
        return canBeSoldAndActiveQuery()
            .apply(ProductEntityOB_.isProductJNC equal isJNC)
            .build().use { it.find() }
    }

    fun updateProductsListing(isJNC: Boolean, productIdsArray: MutableList<Int>) {
        val products = productBox.query(ProductEntityOB_.id.oneOf(productIdsArray.toIntArray()))
            .build().use { it.find() }
        products.forEach {
            insert(it.apply { isProductJNC = isJNC })
        }
    }

    fun updateProductsStockInfo(stockInfoList: List<StockEntityOB>) {
        val products = findAll()
        products.forEach { product ->
            val stockInfo = stockInfoList.find { it.productId == product.id }
            stockInfo?.let { stInfo ->
                product.qoh = stInfo.qoh
                product.rejectOrders = stInfo.rejectOrders
                product.trackStock = stInfo.trackStock
            }
            insert(product)
        }
    }

    fun deleteDeactivatedProducts(productIdsArray: MutableList<Int>): Long =
        productBox.query(ProductEntityOB_.id.oneOf(productIdsArray.toIntArray()))
            .build().use { it.remove() }

    fun getProductsByCode(codes: Array<String>): List<ProductEntityOB> {
        return canBeSoldAndActiveQuery()
            .apply(ProductEntityOB_.code.oneOf(codes))
            .build().use { it.find() }
    }

    fun getPagedProductsByProductCodes(codes: Array<String>): PagingSource<Int, ProductEntityOB> =
        buildPagingSource(
            canBeSoldAndActiveQuery()
                .apply(ProductEntityOB_.code.oneOf(codes))
                .build()
        )

    private fun canBeSoldAndActiveQuery(): QueryBuilder<ProductEntityOB> {
        return if (partnerIsAgent) {
            productBox
                .query(
                    (ProductEntityOB_.canBeSold equal 1)
                        .and(ProductEntityOB_.active equal 1)
                        .and(ProductEntityOB_.isCustomerOnly equal 0 or ProductEntityOB_.isCustomerOnly.isNull)
                )
        } else {
            productBox
                .query(
                    (ProductEntityOB_.canBeSold equal 1)
                        .and(ProductEntityOB_.active equal 1)
                )
        }
    }

    private fun categoryProductsThatCanBeSoldAndActive(categoryId: Int) =
        canBeSoldAndActiveQuery().apply(ProductEntityOB_.categoryId equal categoryId)

    private fun categoryIdsProductsThatCanBeSoldAndActive(categoryIds: IntArray) =
        canBeSoldAndActiveQuery().apply(ProductEntityOB_.categoryId.oneOf(categoryIds))

    private fun buildPagingSource(query: Query<ProductEntityOB>): PagingSource<Int, ProductEntityOB> {
        return ObjectBoxDataSource.Factory(query).asPagingSourceFactory().invoke()
    }

    fun getPagedProductsByPseudoCategoryName(pseudoCategoryName: String): PagingSource<Int, ProductEntityOB> {
        return buildPagingSource(
            canBeSoldAndActiveQuery()
                .apply(
                    pseudoProductAttributeMatcher(pseudoCategoryName)
                )
                .build()
        )
    }

    fun getProductsByPseudoCategoryName(pseudoCategoryName: String): List<ProductEntityOB> {
        return canBeSoldAndActiveQuery()
            .apply(
                pseudoProductAttributeMatcher(pseudoCategoryName)
            ).build().use { it.find() }
    }

    fun getProductsByPseudoCategoryNameSortedByPrice(
        pseudoCategoryName: String,
        isAscending: Int
    ): List<ProductEntityOB> {
        return canBeSoldAndActiveQuery()
            .apply(
                pseudoProductAttributeMatcher(pseudoCategoryName)
            )
            .order(ProductEntityOB_.price, isAscending)
            .build().use { it.find() }
    }

    private fun pseudoProductAttributeMatcher(pseudoCategoryName: String) = when (pseudoCategoryName) {
        Constant.PseudoCategories.APP_ONLY.name.lowercase() -> ProductEntityOB_.appOnly equal 1
        Constant.PseudoCategories.TOP.name.lowercase() -> ProductEntityOB_.topSeller equal 1
        Constant.PseudoCategories.CLEARANCE.name.lowercase() -> ProductEntityOB_.clearance equal 1
        Constant.PseudoCategories.PROMOTE.name.lowercase() -> ProductEntityOB_.promote equal 1
        else -> {
            ProductEntityOB_.categoryId equal 0 // we can't get here really
        }
    }
}
