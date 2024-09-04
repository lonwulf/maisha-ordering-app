package com.lonwulf.home.di

import android.content.Context

import com.lonwulf.core.data.CoreRemoteDataSource
import com.lonwulf.core.data.CoreService
import com.lonwulf.core.data.repository.DataRepositoryImpl
import com.lonwulf.core.data.repository.PartnerRepositoryImpl
import com.lonwulf.core.di.scope.FeatureScope
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.repository.DataRepository
import com.lonwulf.core.domain.repository.PartnerRepository
import com.lonwulf.core.objectboxdb.services.BoxStoreService
import com.lonwulf.core.objectboxdb.services.CartCacheService
import com.lonwulf.core.objectboxdb.services.CategoryEntityService
import com.lonwulf.core.objectboxdb.services.HomePageDataService
import com.lonwulf.core.objectboxdb.services.ProductService
import com.lonwulf.core.objectboxdb.services.PromoService
import com.lonwulf.core.objectboxdb.services.SectionDataService
import com.lonwulf.core.objectboxdb.services.StockService
import com.lonwulf.core.repository.RemoteConfigRepository
import com.lonwulf.core.repository.RemoteConfigRepositoryImpl
import com.lonwulf.category.data.CategoryService
import com.lonwulf.category.data.source.CategoryRemoteDataSource
import com.lonwulf.home.data.HomePageService
import com.lonwulf.home.data.HomepageRepositoryImpl
import com.lonwulf.home.data.source.HomepageRemoteDataSource
import com.lonwulf.home.domain.repositories.HomePageRepository
import com.lonwulf.home.usecase.QuerySectionDataByIdUseCase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class HomeModule {

    @Provides
    @FeatureScope
    fun providesHomePageDataService(boxService: BoxStoreService): HomePageDataService =
        HomePageDataService(boxService)

    @Provides
    @FeatureScope
    fun providesHomePageService(retrofit: Retrofit): HomePageService =
        retrofit.create(HomePageService::class.java)

    @Provides
    @FeatureScope
    fun providesRemoteDataSource(homePageService: HomePageService) =
        HomepageRemoteDataSource(homePageService)

    @Provides
    @FeatureScope
    fun providesCategoryEntityService(boxService: BoxStoreService): CategoryEntityService =
        CategoryEntityService(boxService)

    @Provides
    @FeatureScope
    fun providesProductService(boxService: BoxStoreService): ProductService =
        ProductService(boxService)

    @Provides
    @FeatureScope
    fun providesCartCacheService(boxService: BoxStoreService): CartCacheService =
        CartCacheService(boxService)



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
    fun providesPartnerRepository(
        coreRemoteDataSource: CoreRemoteDataSource,
        dispatcherProvider: CoroutineDispatcherProvider,
        productService: ProductService,
        stockService: StockService
    ): PartnerRepository =
        PartnerRepositoryImpl(
            coreRemoteDataSource = coreRemoteDataSource,
            dispatcher = dispatcherProvider,
            productService = productService
        )

    @Provides
    @FeatureScope
    fun provideQuerySectionDataByIdUseCase(repository: HomePageRepository): QuerySectionDataByIdUseCase {
        return QuerySectionDataByIdUseCase(repository)
    }


    @Provides
    @FeatureScope
    fun provideHomepageRepository(
        dispatcher: CoroutineDispatcherProvider,
        homePageDataService: HomePageDataService,
        sectionDataService: SectionDataService,
        promoService: PromoService,
        categoryEntityService: CategoryEntityService,
        productService: ProductService,
        cartCacheService: CartCacheService,
        remoteDataSource: HomepageRemoteDataSource
    ): HomePageRepository = HomepageRepositoryImpl(
        dispatcher = dispatcher,
        homePageDataService = homePageDataService,
        sectionDataService = sectionDataService,
        promoService = promoService,
        categoryEntityService = categoryEntityService,
        productService = productService,
        cartCacheService = cartCacheService,
        remoteDataSource = remoteDataSource
    )

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
        categoryEntityService: CategoryEntityService,
    ): DataRepository = DataRepositoryImpl(
        dispatcherProvider = dispatcherProvider,
        coreRemoteDataSource = coreRemoteDataSource,
        productService = productService,
        promoService = promoService,
        stockService = stockService,
        homePageDataService = homePageDataService,
        sectionDataService = sectionDataService,
        categoryEntityService = categoryEntityService)

    @Provides
    @FeatureScope
    fun providesCategoryRemoteDataSource(categoryService: CategoryService) =
        CategoryRemoteDataSource(categoryService)

    @Provides
    @FeatureScope
    fun providesCategoryService(retrofit: Retrofit): CategoryService =
        retrofit.create(CategoryService::class.java)
}
