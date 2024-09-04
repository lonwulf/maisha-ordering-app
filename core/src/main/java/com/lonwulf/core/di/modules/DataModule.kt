package com.lonwulf.core.di.modules

import android.content.Context
import com.amazonaws.ClientConfiguration
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.regions.Regions
import com.lonwulf.core.BuildConfig
import com.lonwulf.core.analytics.FirebaseAnalyticsHelper
import com.lonwulf.core.data.CoreRemoteDataSource
import com.lonwulf.core.data.CoreService
import com.lonwulf.core.data.mappers.PseudoCategoryMapper
import com.lonwulf.core.data.repository.AuthenticationRepositoryImpl
import com.lonwulf.core.data.repository.DataRepositoryImpl
import com.lonwulf.core.data.repository.PartnerRepositoryImpl
import com.lonwulf.core.deeplinks.DeepLinkHandler
import com.lonwulf.core.deeplinks.DeepLinkHandlerImpl
import com.lonwulf.core.deeplinks.DeepLinkParser
import com.lonwulf.core.deeplinks.DeepLinkParserImpl
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.repository.AuthenticationRepository
import com.lonwulf.core.domain.repository.DataRepository
import com.lonwulf.core.domain.repository.PartnerRepository
import com.lonwulf.core.flownavigation.FlowDtoToNavigationFlowMapper
import com.lonwulf.core.flownavigation.FlowDtoToNavigationFlowMapperImpl
import com.lonwulf.core.objectboxdb.services.BoxStoreService
import com.lonwulf.core.objectboxdb.services.CategoryEntityService
import com.lonwulf.core.objectboxdb.services.HomePageDataService
import com.lonwulf.core.objectboxdb.services.ProductService
import com.lonwulf.core.objectboxdb.services.PromoService
import com.lonwulf.core.objectboxdb.services.SectionDataService
import com.lonwulf.core.objectboxdb.services.StockService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun providesRemoteDataSource(coreService: CoreService) =
        CoreRemoteDataSource(coreService)

    @Provides
    @Singleton
    fun providesCoreService(retrofit: Retrofit): CoreService =
        retrofit.create(CoreService::class.java)


    @Provides
    @Singleton
    fun providesPartnerRepository(
        coreRemoteDataSource: CoreRemoteDataSource,
        dispatcherProvider: CoroutineDispatcherProvider,
        firebaseAnalyticsHelper: FirebaseAnalyticsHelper,
        productService: ProductService,
    ): PartnerRepository =
        PartnerRepositoryImpl(
            coreRemoteDataSource = coreRemoteDataSource,
            dispatcher = dispatcherProvider,
            firebaseAnalyticsHelper = firebaseAnalyticsHelper,
            productService = productService
        )


    @Provides
    @Singleton
    fun providesCategoryEntityService(boxService: BoxStoreService): CategoryEntityService =
        CategoryEntityService(boxService)

    @Provides
    @Singleton
    fun provideDataRepository(
        coreRemoteDataSource: CoreRemoteDataSource,
        dispatcherProvider: CoroutineDispatcherProvider,
        promoService: PromoService,
        productService: ProductService,
        sectionDataService: SectionDataService,
        stockService: StockService,
        homePageDataService: HomePageDataService,
        firebaseAnalyticsHelper: FirebaseAnalyticsHelper,
        categoryEntityService: CategoryEntityService): DataRepository = DataRepositoryImpl(
        dispatcherProvider = dispatcherProvider,
        coreRemoteDataSource = coreRemoteDataSource,
        productService = productService,
        promoService = promoService,
        stockService = stockService,
        homePageDataService = homePageDataService,
        sectionDataService = sectionDataService,
        firebaseAnalyticsHelper = firebaseAnalyticsHelper,
        categoryEntityService = categoryEntityService)

    @Provides
    @Singleton
    fun providesPromoService(boxService: BoxStoreService): PromoService =
        PromoService(boxService)

    @Provides
    @Singleton
    fun providesSectionDataService(boxService: BoxStoreService): SectionDataService =
        SectionDataService(boxService)

    @Provides
    @Singleton
    fun provideStockService(boxService: BoxStoreService): StockService =
        StockService(boxService)

    @Provides
    @Singleton
    fun providesHomePageDataService(boxService: BoxStoreService): HomePageDataService =
        HomePageDataService(boxService)

}
