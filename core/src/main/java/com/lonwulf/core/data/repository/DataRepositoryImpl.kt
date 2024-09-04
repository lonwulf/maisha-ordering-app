package com.lonwulf.core.data.repository

import com.lonwulf.core.analytics.FirebaseAnalyticsHelper
import com.lonwulf.core.data.CoreRemoteDataSource
import com.lonwulf.core.data.mappers.mapApiResponseToCategoryEntity
import com.lonwulf.core.data.mappers.mapApiResponseToHomepageEntity
import com.lonwulf.core.data.mappers.mapApiResponseToPreviousAgentEntity
import com.lonwulf.core.data.mappers.mapApiResponseToProductEntity
import com.lonwulf.core.data.mappers.mapApiResponseToPromoEntity
import com.lonwulf.core.data.mappers.mapApiResponseToStockEntity
import com.lonwulf.core.data.mappers.mapCategoryDataToDomain
import com.lonwulf.core.data.mappers.mapDTOListToModelList
import com.lonwulf.core.data.mappers.mapHomePageUrls
import com.lonwulf.core.data.mappers.mapSectionDataEntityToHomepageEntity
import com.lonwulf.core.data.mappers.mapToHomeDeliveryList
import com.lonwulf.core.data.response.partner.AgentDto
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.models.Category
import com.lonwulf.core.domain.models.HomeDelivery
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.domain.models.ProductProgramModel
import com.lonwulf.core.domain.repository.DataRepository
import com.lonwulf.core.mapper.mapProductEntityToDomainWithPromos
import com.lonwulf.core.network.ApiResponse
import com.lonwulf.core.objectboxdb.entity.CategoryEntityOB
import com.lonwulf.core.objectboxdb.entity.HomePageEntityOB
import com.lonwulf.core.objectboxdb.entity.ProductEntityOB
import com.lonwulf.core.objectboxdb.entity.PromoEntityOB
import com.lonwulf.core.objectboxdb.entity.SectionDataEntityOB
import com.lonwulf.core.objectboxdb.services.CategoryEntityService
import com.lonwulf.core.objectboxdb.services.HomePageDataService
import com.lonwulf.core.objectboxdb.services.ProductService
import com.lonwulf.core.objectboxdb.services.PromoService
import com.lonwulf.core.objectboxdb.services.SectionDataService
import com.lonwulf.core.objectboxdb.services.StockService
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Constant.homePageImages
import com.lonwulf.core.utils.Prefs
import com.lonwulf.core.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import timber.log.Timber
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val promoService: PromoService,
    private val stockService: StockService,
    private val productService: ProductService,
    private val previousRAService: PreviousRAService,
    private val sectionDataService: SectionDataService,
    private val homePageDataService: HomePageDataService,
    private val coreRemoteDataSource: CoreRemoteDataSource,
    private val categoryEntityService: CategoryEntityService,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val firebaseAnalyticsHelper: FirebaseAnalyticsHelper,
) : DataRepository {

    override suspend fun fetchAndInsertPromos(channel: String): Result<List<PromoEntityOB>> {
        val apiResultFromConfig = coreRemoteDataSource.fetchPromosFromConfig(dispatcherProvider.io)
        val apiResultFromChannelSpecificPromos =
            coreRemoteDataSource.fetchChannelSpecificPromos(dispatcherProvider.io, channel)
        val results = mutableListOf<PromoEntityOB>()
        val allProducts = productService.findAll()
        when (apiResultFromConfig) {
            is Result.Error -> {
                // ignore error silently
            }

            Result.Loading -> {
                // ignore
            }

            is Result.Success -> {
                val promos = apiResultFromConfig.data.data.mapApiResponseToPromoEntity(
                    channel = channel,
                    allProducts = allProducts
                )
                results.addAll(promos)
            }
        }
        when (apiResultFromChannelSpecificPromos) {
            is Result.Error -> {
                // ignore error silently
            }

            Result.Loading -> {
                // ignore
            }

            is Result.Success -> {
                val promos =
                    apiResultFromChannelSpecificPromos.data.data.mapApiResponseToPromoEntity(
                        channel = channel,
                        allProducts = allProducts
                    )
                results.addAll(promos)
            }
        }

        promoService.deleteAll()
        promoService.insertAll(results)
        return Result.Success(emptyList())
    }


    override suspend fun fetchAndInsertProducts(): Result<Boolean> {
        val scope = CoroutineScope(Dispatchers.IO)
        val productResultDeferred = scope.async {
            coreRemoteDataSource.fetchProducts(Dispatchers.IO)
        }
        val deactivatedProductsResultDeferred = scope.async {
            coreRemoteDataSource.fetchDeactivatedProducts(Dispatchers.IO)
        }
        val jncProductsResultDeferred = scope.async {
            coreRemoteDataSource.fetchJncProducts(Dispatchers.IO)
        }

        val productResult = productResultDeferred.await()
        val deactivatedProductsResult = deactivatedProductsResultDeferred.await()
        val jncProductsResult = jncProductsResultDeferred.await()
        val prodItems: List<ProductEntityOB>?
        when (productResult) {
            is Result.Loading -> {
            }

            is Result.Success -> {
                prodItems = productResult.data.mapApiResponseToProductEntity()
                Constant.productImages.clear()
                Prefs.putBoolean(Constant.PrefKeys.PRODUCTS_SYNCED, true)
                if (Prefs.getString(Constant.PrefKeys.PARTNER_TYPE) == Constant.UserType.AGENT) {
                    Prefs.putBoolean(Constant.PrefKeys.PRICE_LIST_SYNCED, true)
                }
                prodItems.forEach { product ->
                    product.image?.url?.let {
                        val imageUrl = Constant.getBaseImageUrl() + it
                        Constant.productImages.add(imageUrl)
                    }
                }
                productService.insertAll(prodItems)
            }

            is Result.Error -> {
            }
        }
        when (deactivatedProductsResult) {
            is Result.Loading -> {
            }

            is Result.Success -> {
                Prefs.putDouble(
                    Constant.PrefKeys.DEACTIVATED_LAST_SYNC_TIME_STAMP,
                    deactivatedProductsResult.data.data?.latest_sync_date ?: 0.0
                )
                val productIdsList = deactivatedProductsResult.data.data?.products
                productIdsList?.let { productService.deleteDeactivatedProducts(it.toMutableList()) }
            }

            is Result.Error -> {
                Timber.e("Failed to fetch deactivated product: ${deactivatedProductsResult.errorMessage}")
            }
        }


        return Result.Success(true)
    }

    override suspend fun fetchDeactivatedProducts(): Result<Long> {
        return when (
            val apiResult =
                coreRemoteDataSource.fetchDeactivatedProducts(dispatcherProvider.io)
        ) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                Prefs.putDouble(
                    Constant.PrefKeys.DEACTIVATED_LAST_SYNC_TIME_STAMP,
                    apiResult.data.data?.latest_sync_date ?: 0.0
                )
                val result = apiResult.data.data?.products?.let {
                    productService.deleteDeactivatedProducts(it.toMutableList())
                } ?: 0
                Result.Success(result)
            }

            is Result.Error -> {
                firebaseAnalyticsHelper.logAppError(
                    apiResult.errorMessage ?: "error",
                    apiResult.code ?: 0
                )
                Result.Error(apiResult.cause, apiResult.code, apiResult.errorMessage)
            }
        }
    }

    override suspend fun fetchProductStockQuantity(): Result<Boolean> {
        Timber.d(javaClass.simpleName.plus(" ").plus("fetch stock quantity start"))
        return when (
            val apiResult =
                coreRemoteDataSource.fetchProductQuantity(dispatcherProvider.io)
        ) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                val stockInfo = apiResult.data.mapApiResponseToStockEntity()
                stockInfo.takeIf { it.isNotEmpty() }?.let {
                    stockService.deleteAll()
                    stockService.insertAll(it)
                    // update products with stock info
                    productService.updateProductsStockInfo(it)
                }
                Result.Success(true)
            }

            is Result.Error -> {
                firebaseAnalyticsHelper.logAppError(
                    apiResult.errorMessage ?: "error",
                    apiResult.code ?: 0
                )
                Result.Error(apiResult.cause, apiResult.code, apiResult.errorMessage)
            }
        }
    }

    override suspend fun fetchHomePage(): Result<Map<HomePageEntityOB, List<SectionDataEntityOB>>> {
        return when (val apiResult = coreRemoteDataSource.fetchHomePage(dispatcherProvider.io)) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                val homePageAndSectionDataPair =
                    apiResult.data.data?.mapApiResponseToHomepageEntity()
                val homePageItems = homePageAndSectionDataPair?.first?.filter { it.active }
                val sectionDataItems = homePageAndSectionDataPair?.second
                homePageDataService.deleteAll()
                sectionDataService.deleteAll()
                homePageImages.clear()
                homePageItems?.let {
                    homePageDataService.insertAll(homePageItems)
                }
                sectionDataItems?.let {
                    sectionDataService.insertAll(sectionDataItems)
                    homePageImages.addAll(sectionDataItems.mapHomePageUrls())
                }

//                EventBus.getDefault().post(HomePageSyncEvent(syncImages = true))

                Result.Success(findAllSectionsAndSectionDataItems())
            }

            is Result.Error -> {
                Timber.e(apiResult.toString())
                firebaseAnalyticsHelper.logAppError(
                    apiResult.errorMessage ?: "error",
                    apiResult.code ?: 0
                )
                Result.Error(apiResult.cause, apiResult.code, apiResult.errorMessage)
            }
        }
    }

    override suspend fun findAllSectionsAndSectionDataItems(): Map<HomePageEntityOB, List<SectionDataEntityOB>> {
        return mapSectionDataEntityToHomepageEntity(
            homePageDataService.findAll(),
            sectionDataService.findAll()
        )
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

    override suspend fun fetchCategories(): Result<List<CategoryEntityOB>> {
        return when (val apiResult = coreRemoteDataSource.fetchCategories(dispatcherProvider.io)) {
            is Result.Loading -> Result.Loading
            is Result.Success -> {
                val catItems = apiResult.data.mapApiResponseToCategoryEntity()

                categoryEntityService.insertAll(catItems)
                Constant.categoryImages.clear()
                for (category in catItems) {
                    val extension = category.image.let { it?.url }
                    if (extension != null) {
                        val imageUrl = Constant.getBaseImageUrl() + extension
                        Constant.categoryImages.add(imageUrl)
                    }
                }
                Result.Success(emptyList())
            }

            is Result.Error -> {
                firebaseAnalyticsHelper.logAppError(
                    apiResult.errorMessage ?: "error",
                    apiResult.code ?: 0
                )
                Result.Error(apiResult.cause, apiResult.code, apiResult.errorMessage)
            }
        }
    }

    override suspend fun getParentCategories(): List<Category> {
        return categoryEntityService.getSubCategoryByParentId().map {
            it.mapCategoryDataToDomain()
        }
    }

    override suspend fun getSubCategoryByParentId(categoryId: Int): List<Category> {
        return categoryEntityService.getSubCategoryByParentId(categoryId).map {
            it.mapCategoryDataToDomain()
        }
    }

    override suspend fun fetchCustomerStatus(): Result<Boolean> {
        return when (val apiResult = coreRemoteDataSource.fetchCustomerStatus(dispatcherProvider.io)) {
            is Result.Loading -> Result.Loading
            is Result.Error -> {
                firebaseAnalyticsHelper.logAppError(
                    apiResult.errorMessage ?: "error",
                    apiResult.code ?: 0
                )
                Result.Error(apiResult.cause, apiResult.code, apiResult.errorMessage)
            }

            is Result.Success -> {
                apiResult.data.data?.let { data ->
                    Prefs.putBoolean(Constant.PrefKeys.CUSTOMER_COD, data.cod ?: false)
                    Prefs.putBoolean(Constant.PrefKeys.CUSTOMER_VERIFIED, data.verified ?: false)
                    Prefs.putLong(Constant.PrefKeys.CUSTOMER_AGENT_ID, data.agent_id ?: 0L)
                    Prefs.putDouble(
                        Constant.PrefKeys.CUSTOMER_AMOUNT_LIMIT,
                        data.pay_later_limit ?: 0.0
                    )
                }
                Result.Success(true)
            }
        }
    }

}
