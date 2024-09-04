package com.lonwulf.home.data

import com.lonwulf.core.analytics.FirebaseAnalyticsHelper
import com.lonwulf.core.data.mappers.mapCategoryDataToDomain
import com.lonwulf.core.data.mappers.mapProductToCartCacheEntity
import com.lonwulf.core.data.mappers.mapPseudoCategoryEntityToDomain
import com.lonwulf.core.data.mappers.mapSectionDataEntityToHomepageEntity
import com.lonwulf.core.data.mappers.mapSectionDataToDomain
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.models.Category
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.domain.models.PseudoCategory
import com.lonwulf.core.domain.models.SectionData
import com.lonwulf.core.mapper.mapProductEntityToDomain
import com.lonwulf.core.mapper.mapProductEntityToDomainWithPromos
import com.lonwulf.core.objectboxdb.entity.HomePageEntityOB
import com.lonwulf.core.objectboxdb.entity.ProductEntityOB
import com.lonwulf.core.objectboxdb.entity.SectionDataEntityOB
import com.lonwulf.core.objectboxdb.services.CartCacheService
import com.lonwulf.core.objectboxdb.services.CategoryEntityService
import com.lonwulf.core.objectboxdb.services.HomePageDataService
import com.lonwulf.core.objectboxdb.services.ProductService
import com.lonwulf.core.objectboxdb.services.PromoService
import com.lonwulf.core.objectboxdb.services.PseudoCategoryService
import com.lonwulf.core.objectboxdb.services.SectionDataService
import com.lonwulf.core.utils.Result
import com.lonwulf.home.data.response.LocationDto
import com.lonwulf.home.data.source.HomepageRemoteDataSource
import com.lonwulf.home.domain.repositories.HomePageRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

@Suppress("LongParameterList", "WildcardImport")
class HomepageRepositoryImpl @Inject constructor(
    val dispatcher: CoroutineDispatcherProvider,
    private val homePageDataService: HomePageDataService,
    private val sectionDataService: SectionDataService,
    private val promoService: PromoService,
    private val categoryEntityService: CategoryEntityService,
    private val productService: ProductService,
    private val cartCacheService: CartCacheService,
    private val remoteDataSource: HomepageRemoteDataSource,
) : HomePageRepository {

    override suspend fun findAllSectionsAndSectionDataItems(): Map<HomePageEntityOB, List<SectionDataEntityOB>> {
        return mapSectionDataEntityToHomepageEntity(
            homePageDataService.findAll(),
            sectionDataService.findAll()
        )
    }

    override suspend fun getSectionDataBySectionId(sectionId: Int): List<SectionData> {
        return sectionDataService.getSectionDataBySectionId(sectionId)
            .map {
                it.mapSectionDataToDomain(
                    getSectionProductsByIds(listOf(it))
                )
            }
    }

    override suspend fun getSectionProductsByIds(sections: List<SectionDataEntityOB>): List<Product> {
        val products = arrayListOf<Product>()
        val productIds = mutableListOf<Int>()
        sections.map {
            productIds.addAll(it.productIds?.toMutableList() ?: mutableListOf())
        }
        val promos = promoService.findAll()
        val productsListFromDB = productService.findProductsByProductIds(productIds)
        products.addAll(
            mapProductEntityToDomainWithPromos(productsListFromDB, promos)
        )
        return products
    }

    override suspend fun deleteHomePageUseCase() {
        homePageDataService.deleteAll()
    }

    override suspend fun getSubCategoryByParentId(categoryId: Int): List<Category> {
        return categoryEntityService.getSubCategoryByParentId(categoryId).map {
            it.mapCategoryDataToDomain()
        }
    }

    override suspend fun getProductByCode(productCode: String): Product? {
        return productService.getProductByCode(productCode)?.mapProductEntityToDomain()
    }

    override suspend fun getProductsByCategoryId(categoryId: Int): List<Product> {
        return productService.getProductsByCategoryId(categoryId)
            .map { it.mapProductEntityToDomain() }
    }

    /**
     * Defines the source of data and how to retrieve the data
     */
    override suspend fun getRecommendedProductsByCategoryId(categoryId: IntArray): Collection<Product> {
        return productService.getRecommendProductsByCategoryId(categoryId)
            .map { it.mapProductEntityToDomain() }
    }

    override suspend fun getRecommendedProductsByBrandNames(brandNames: Array<String>): Collection<Product> {
        return productService.getRecommendProductsByBrands(brandNames[0])
            .map { it.mapProductEntityToDomain() }
    }

    override suspend fun getProductsWithProductCodes(productCodesArray: Array<String>): Collection<Product> {
        return productService.getProductsWithCodes(productCodesArray)
            .map { it.mapProductEntityToDomain() }
    }

    override suspend fun insertItemToCart(item: Product): Long {
        item.mapProductToCartCacheEntity()
            .apply {
                return cartCacheService.insertItemToCart(this)
            }
    }

    override suspend fun updateProductFavourite(productCode: String?): Product? {
        val productItem: ProductEntityOB = productCode?.let { code ->
            productService.getProductByCode(code)?.also {
                it.isFavourite = !it.isFavourite
            }
        } ?: return null
        productService.insert(productItem)
        return productItem.mapProductEntityToDomain()
    }

    override suspend fun getCartCount(): Int {
        return cartCacheService.getNumberOfCartItems()
    }

    override suspend fun clearHomePageData() {
        homePageDataService.deleteAll()
        sectionDataService.deleteAll()
    }

    override suspend fun updateCustomerLocation(coordinates: LatLng): Result<LocationDto> {
        return when (
            val apiResult =
                remoteDataSource.updateCustomerLocation(dispatcher.io, coordinates)
        ) {
            is Result.Loading -> Result.Loading
            is Result.Success -> Result.Success(apiResult.data)
            is Result.Error -> {
                firebaseAnalyticsHelper.logAppError(
                    apiResult.errorMessage ?: "error",
                    apiResult.code ?: 0
                )
                Result.Error(apiResult.cause, apiResult.code, apiResult.errorMessage)
            }
        }
    }

    override suspend fun getSectionDataById(sectionId: Int): SectionData? {
        val sectionData = sectionDataService.getSectionDataById(sectionId) ?: return null
        val mappedToDomain = sectionData.mapSectionDataToDomain(
            getSectionProductsByIds(listOf(sectionData))
        )
        return mappedToDomain
    }

    override suspend fun getSubCategoryById(subCategoryId: Int): Category? {
        return categoryEntityService.getSubCategoryById(subCategoryId).map {
            it.mapCategoryDataToDomain()
        }.firstOrNull()
    }

    override suspend fun getParentCategories(): List<Category> {
        return categoryEntityService.getSubCategoryByParentId().map {
            it.mapCategoryDataToDomain()
        }
    }
}
