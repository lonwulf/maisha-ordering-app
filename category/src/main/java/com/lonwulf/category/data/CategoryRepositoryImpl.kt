package com.lonwulf.category.data

import androidx.paging.PagingSource
import com.lonwulf.core.data.mappers.mapCategoryDataToDomain
import com.lonwulf.core.data.mappers.mapProductToCartCacheEntity
import com.lonwulf.core.data.mappers.mapPseudoCategoryEntityToDomain
import com.lonwulf.core.domain.models.Category
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.mapper.mapProductDomainToEntity
import com.lonwulf.core.mapper.mapProductEntityToDomain
import com.lonwulf.core.mapper.mapProductSavings
import com.lonwulf.core.objectboxdb.entity.ProductEntityOB
import com.lonwulf.core.objectboxdb.entity.PromoEntityOB
import com.lonwulf.core.objectboxdb.entity.SectionDataEntityOB
import com.lonwulf.core.objectboxdb.services.CartCacheService
import com.lonwulf.core.objectboxdb.services.CategoryEntityService
import com.lonwulf.core.objectboxdb.services.ProductService
import com.lonwulf.core.objectboxdb.services.PromoService
import com.lonwulf.core.objectboxdb.services.SectionDataService
import com.lonwulf.category.domain.repositories.CategoryRepository
import timber.log.Timber
import javax.inject.Inject

@Suppress("LongParameterList")
class CategoryRepositoryImpl @Inject constructor(
    private val categoryEntityService: CategoryEntityService,
    private val productService: ProductService,
    private val promoService: PromoService,
    private val cartCacheService: CartCacheService,
    private val sectionDataService: SectionDataService
) : CategoryRepository {

    override suspend fun findAllCategories(): List<Category> {
        return categoryEntityService.findAll().map { it.mapCategoryDataToDomain() }
    }

    override fun getPagedProductsByCode(codes: ArrayList<String>): PagingSource<Int, ProductEntityOB> =
        productService.getPagedProductsByProductCodes(codes.toTypedArray())

    override fun getProductsByCode(codes: ArrayList<String>): List<Product> {
        return productService.getProductsByCode(codes.toTypedArray())
            .map { product ->
                product.mapProductEntityToDomain().apply {
                    mapProductSavings(
                        this,
                        promoService.getPromoForProductId(this.id)
                    )
                }
            }
    }

    override suspend fun updateProductFavourite(product: Product): Product {
        try {
            var productItem: ProductEntityOB? = productService.getProductByCode(product.code)
            // Do not refactor this tp productItem.code == null because of NPE : ProductEntity.getCode()' on a null object reference
            if (productItem == null) {
                product.favorite = true
                productItem = product.mapProductDomainToEntity()
                productService.insert(productItem)
            } else {
                productItem.let {
                    it.isFavourite = !it.isFavourite
                }
                productService.update(productItem)
            }
            return productItem.mapProductEntityToDomain().apply {
                mapProductSavings(
                    product,
                    promoService.getPromoForProductId(product.id)
                )
            }
        } catch (e: Exception) {
            product.favorite = !product.favorite
            return product
        }
    }

    override suspend fun insertItemToCart(item: Product): Long {
        item.mapProductToCartCacheEntity()
            .apply {
                return cartCacheService.insertItemToCart(this)
            }
    }

    override suspend fun updateItemQuantity(
        productCode: String,
        quantity: Int
    ): Int = cartCacheService.updateCartItem(productCode, quantity)

    override suspend fun searchForProducts(search: String): List<Product> {
        return productService.getSearchProductResult(search)
            .map { it.mapProductEntityToDomain() }
            .map {
                mapProductSavings(
                    it,
                    promoService.getPromoForProductId(it.id)
                )
            }
    }

    override suspend fun getCartCount(): Int {
        return cartCacheService.getNumberOfCartItems()
    }

    override suspend fun getRecommendedProductsByCategoryId(categoryId: IntArray): Collection<Product> {
        return productService.getRecommendProductsByCategoryId(categoryId)
            .map { it.mapProductEntityToDomain() }
            .map {
                mapProductSavings(
                    it,
                    promoService.getPromoForProductId(it.id)
                )
            }
    }

    override suspend fun getAllTmplProducts(tmplId: Int): List<Product> {
        return productService.getAllTmplProducts(tmplId)
            .map { it.mapProductEntityToDomain() }
            .map {
                mapProductSavings(
                    it,
                    promoService.getPromoForProductId(it.id)
                )
            }
    }

    override fun getPagedRecommendProductsByCategoryId(categoryId: IntArray): PagingSource<Int, ProductEntityOB> {
        return productService.getPagedRecommendProductsByCategoryId(categoryId)
    }

    override suspend fun updateProductsListing(
        isJNC: Boolean,
        productIdsListArray: MutableList<Int>
    ) {
        productService.updateProductsListing(isJNC, productIdsListArray)
    }

    override suspend fun getProductsByCategoryId(categoryId: Int): List<Product> {
        return productService.getProductsByCategoryId(categoryId)
            .map { it.mapProductEntityToDomain() }
            .map {
                mapProductSavings(
                    it,
                    promoService.getPromoForProductId(it.id)
                )
            }
    }

    override fun getPagedProductsByCategoryId(categoryId: Int): PagingSource<Int, ProductEntityOB> {
        return productService.getPagedProductsByCategoryId(categoryId)
    }

    override fun getPagedProductsByCategoryIdSortedByPrice(
        categoryId: Int,
        isAscending: Int
    ): PagingSource<Int, ProductEntityOB> {
        return productService.getPagedProductsByCategoryIdSortedByPrice(categoryId, isAscending)
    }

    override fun getProductsByCategoryIdSortedByPrice(
        categoryId: Int,
        isAscending: Int
    ): List<Product> {
        return productService.getProductsByCategoryIdSortedByPrice(categoryId, isAscending)
            .map { it.mapProductEntityToDomain() }
            .map {
                mapProductSavings(
                    it,
                    promoService.getPromoForProductId(it.id)
                )
            }
    }


    override fun getPagedSearchProductResult(searchItem: String): PagingSource<Int, ProductEntityOB> {
        return productService.getPagedSearchProductResult(searchItem.uppercase())
    }

    override suspend fun getProductByCode(productCode: String): Product? {
        return productService.getProductByCode(productCode)?.let {
            it.mapProductEntityToDomain().apply {
                mapProductSavings(
                    this,
                    promoService.getPromoForProductId(this.id)
                )
            }
        }
    }

    override suspend fun deleteCategories() {
        Timber.e(javaClass.simpleName.plus(" ").plus("categoryDao.deleteAll").plus(" "))
        categoryEntityService.deleteAll()
    }

    override suspend fun getParentCategories(): List<Category> {
        return categoryEntityService.getSubCategoryByParentId().map {
            it.mapCategoryDataToDomain()
        }
    }

    override suspend fun getSubCategoryByParentId(parentId: Int): List<Category> {
        return categoryEntityService.getSubCategoryByParentId(parentId).map {
            it.mapCategoryDataToDomain()
        }
    }

    override suspend fun findAllProducts(): List<Product> {
        return productService.findAll().map {
            it.mapProductEntityToDomain()
        }.map {
            mapProductSavings(it, promoService.getPromoForProductId(it.id))
        }
    }

    override fun findAllPagedProducts(): PagingSource<Int, ProductEntityOB> {
        return productService.findAllPagedProducts()
    }

    override fun findAllProductEntity(): List<ProductEntityOB> {
        return productService.findAllProductEntity()
    }

    override fun getSectionDataByIdPaged(sectionDataId: Int): PagingSource<Int, SectionDataEntityOB> {
        return sectionDataService.getSectionDataByIdPaged(sectionDataId)
    }

    override suspend fun getProductsBySectionDataId(sectionDataId: Int): List<Product> {
        val products = mutableListOf<Product>()
        sectionDataService.getSectionDataById(sectionDataId)?.let { sectionData ->
            products.addAll(getSectionProductsByIds(sectionData.productIds ?: listOf()))
        }
        return products.toList()
    }

    override fun getPromoByProductId(productId: Long?): PromoEntityOB? {
        return promoService
            .getPromoForProductId(productId)
    }

    override suspend fun getSectionProductsByIds(productIds: List<Int>): List<Product> {
        return productService.findProductsByProductIds(productIds.toMutableList())
            .map { product ->
                product.mapProductEntityToDomain().apply {
                    mapProductSavings(
                        this,
                        promoService.getPromoForProductId(this.id)
                    )
                }
            }
    }

}
