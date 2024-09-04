package com.lonwulf.category.di

import android.content.Context
import com.lonwulf.core.analytics.CallToOrderAnalyticsInterface
import com.lonwulf.core.analytics.CallToOrderImpl
import com.lonwulf.core.analytics.CategoryAnalyticsImpl
import com.lonwulf.core.analytics.CategoryAnalyticsInterface
import com.lonwulf.core.analytics.FacebookEventsAnalyticsImpl
import com.lonwulf.core.analytics.FacebookEventsAnalyticsInterface
import com.lonwulf.core.analytics.FavoritesAnalyticsImpl
import com.lonwulf.core.analytics.FavoritesAnalyticsInterface
import com.lonwulf.core.analytics.FbAppEventsHelper
import com.lonwulf.core.analytics.FirebaseAnalyticsHelper
import com.lonwulf.core.analytics.SearchAnalyticsImpl
import com.lonwulf.core.analytics.SearchAnalyticsInterface
import com.lonwulf.core.data.CoreRemoteDataSource
import com.lonwulf.core.data.CoreService
import com.lonwulf.core.data.repository.DataRepositoryImpl
import com.lonwulf.core.di.scope.FeatureScope
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.repository.DataRepository
import com.lonwulf.core.objectboxdb.services.BoxStoreService
import com.lonwulf.core.objectboxdb.services.CartCacheService
import com.lonwulf.core.objectboxdb.services.CategoryEntityService
import com.lonwulf.core.objectboxdb.services.HomePageDataService
import com.lonwulf.core.objectboxdb.services.PreviousRAService
import com.lonwulf.core.objectboxdb.services.ProductService
import com.lonwulf.core.objectboxdb.services.PromoService
import com.lonwulf.core.objectboxdb.services.PseudoCategoryService
import com.lonwulf.core.objectboxdb.services.SectionDataService
import com.lonwulf.core.objectboxdb.services.StockService
import com.lonwulf.core.repository.RemoteConfigRepository
import com.lonwulf.core.repository.RemoteConfigRepositoryImpl
import com.lonwulf.category.data.CategoryService
import com.lonwulf.category.data.source.CategoryRemoteDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class CategoryModule {

    @Provides
    @FeatureScope
    fun providesCategoryEntityService(
        boxService: BoxStoreService
    ): CategoryEntityService = CategoryEntityService(boxService)

    @Provides
    @FeatureScope
    fun providesProductService(boxService: BoxStoreService): ProductService =
        ProductService(boxService)

    @Provides
    @FeatureScope
    fun providesPromoService(boxService: BoxStoreService): PromoService = PromoService(boxService)


    @Provides
    @FeatureScope
    fun providesCategoryService(retrofit: Retrofit): CategoryService =
        retrofit.create(CategoryService::class.java)

    @Provides
    @FeatureScope
    fun providesRemoteDataSource(categoryService: CategoryService) =
        CategoryRemoteDataSource(categoryService)

    @Provides
    @FeatureScope
    fun providesCartCacheService(boxService: BoxStoreService): CartCacheService =
        CartCacheService(boxService)


    @Provides
    @FeatureScope
    fun providesSectionDataService(boxService: BoxStoreService): SectionDataService =
        SectionDataService(boxService)

    @Provides
    @FeatureScope
    fun remoteConfigRepository(firebaseAnalyticsHelper: FirebaseAnalyticsHelper): RemoteConfigRepository {
        return RemoteConfigRepositoryImpl(firebaseAnalyticsHelper)
    }

    @Provides
    @FeatureScope
    fun providesStockService(boxService: BoxStoreService): StockService = StockService(boxService)

    @Provides
    @FeatureScope
    fun providesCoreRemoteDataSource(coreService: CoreService) =
        CoreRemoteDataSource(coreService)

    @Provides
    @FeatureScope
    fun providesCoreService(retrofit: Retrofit): CoreService =
        retrofit.create(CoreService::class.java)

    @Provides
    @FeatureScope
    fun provideDataRepository(
        coreRemoteDataSource: CoreRemoteDataSource,
        dispatcherProvider: CoroutineDispatcherProvider,
        promoService: PromoService,
        productService: ProductService,
        sectionDataService: SectionDataService,
        stockService: StockService,
        homePageDataService: HomePageDataService,
        categoryEntityService: CategoryEntityService
    ): DataRepository = DataRepositoryImpl(
        dispatcherProvider = dispatcherProvider,
        coreRemoteDataSource = coreRemoteDataSource,
        productService = productService,
        promoService = promoService,
        stockService = stockService,
        homePageDataService = homePageDataService,
        sectionDataService = sectionDataService,
        categoryEntityService = categoryEntityService
    )
}
