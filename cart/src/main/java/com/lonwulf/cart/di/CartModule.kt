package com.lonwulf.cart.di

import android.content.Context
import com.lonwulf.core.analytics.CartAnalyticsImpl
import com.lonwulf.core.analytics.CartAnalyticsInterface
import com.lonwulf.core.analytics.CategoryAnalyticsImpl
import com.lonwulf.core.analytics.CategoryAnalyticsInterface
import com.lonwulf.core.analytics.FacebookEventsAnalyticsImpl
import com.lonwulf.core.analytics.FacebookEventsAnalyticsInterface
import com.lonwulf.core.analytics.FbAppEventsHelper
import com.lonwulf.core.analytics.FirebaseAnalyticsHelper
import com.lonwulf.core.analytics.RASelectionAnalyticsImpl
import com.lonwulf.core.analytics.RASelectionAnalyticsInterface
import com.lonwulf.core.data.CoreRemoteDataSource
import com.lonwulf.core.data.CoreService
import com.lonwulf.core.data.mappers.ReceivingAgentMapper
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
import com.lonwulf.core.objectboxdb.services.PreviousRAService
import com.lonwulf.core.objectboxdb.services.ProductService
import com.lonwulf.core.objectboxdb.services.PromoService
import com.lonwulf.core.objectboxdb.services.PseudoCategoryService
import com.lonwulf.core.objectboxdb.services.SectionDataService
import com.lonwulf.core.objectboxdb.services.StockService
import com.lonwulf.core.repository.RTDBRepository
import com.lonwulf.core.repository.RTDBRepositoryImpl
import com.lonwulf.core.repository.RemoteConfigRepository
import com.lonwulf.core.repository.RemoteConfigRepositoryImpl
import com.lonwulf.cart.data.CartService
import com.lonwulf.cart.data.mapper.CacheMapper
import com.lonwulf.cart.data.source.CartModuleRemoteDataSource
import com.lonwulf.cart.domain.repository.CartRepository
import com.lonwulf.cart.domain.usecase.AgentSearchUseCase
import com.lonwulf.cart.domain.usecase.AgentsOnMapUseCase
import com.lonwulf.cart.domain.usecase.CartItemCountUseCase
import com.lonwulf.cart.domain.usecase.CartListUseCase
import com.lonwulf.cart.domain.usecase.ConfirmOrderUseCase
import com.lonwulf.cart.domain.usecase.DeleteAnItemUseCase
import com.lonwulf.cart.domain.usecase.DeleteMultipleItemsUseCase
import com.lonwulf.cart.domain.usecase.DeleteNonJncProductsUseCase
import com.lonwulf.cart.domain.usecase.GetAllItemsUseCase
import com.lonwulf.cart.domain.usecase.InsertToCartUseCase
import com.lonwulf.cart.domain.usecase.UpdateQuantityUseCase
import com.lonwulf.cart.domain.usecase.ValidateCouponUseCase
import com.lonwulf.cart.domain.usecase.ValidateQuantityUseCase
import com.lonwulf.feature_loan.data.LoansService
import com.lonwulf.feature_loan.data.source.LoansRemoteDataSource
import com.lonwulf.feature_loan.domain.repositories.LoansRepository
import com.lonwulf.feature_loan.domain.usecases.GetLoanBalancesUseCase
import com.lonwulf.feature_loan.domain.usecases.GetLoanDetailsUseCase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class CartModule {

    @Provides
    @FeatureScope
    fun providesCartService(retrofit: Retrofit): CartService =
        retrofit.create(CartService::class.java)

    @Provides
    @FeatureScope
    fun providesCoreService(retrofit: Retrofit): CoreService =
        retrofit.create(CoreService::class.java)

    @Provides
    @FeatureScope
    fun providesCoreRemoteDataSource(coreService: CoreService) =
        CoreRemoteDataSource(coreService)

    @Provides
    @FeatureScope
    fun providesPromoService(boxService: BoxStoreService): PromoService =
        PromoService(boxService)

    @Provides
    @FeatureScope
    fun providesProductService(boxService: BoxStoreService): ProductService =
        ProductService(boxService)

    @Provides
    @FeatureScope
    fun providesRemoteDataSource(cartService: CartService) =
        CartModuleRemoteDataSource(cartService)

    @Provides
    @FeatureScope
    fun provideCartItemMapper(): CacheMapper {
        return CacheMapper()
    }


    @Provides
    @FeatureScope
    fun provideCartListInteractors(
        shoppingCartCacheDataSource: ShoppingCartCacheDataSource,
        repository: CartRepository
    ): CartListUseCase {
        return CartListUseCase(
            CartItemCountUseCase(shoppingCartCacheDataSource = shoppingCartCacheDataSource),
            DeleteAnItemUseCase(shoppingCartCacheDataSource = shoppingCartCacheDataSource),
            DeleteMultipleItemsUseCase(shoppingCartCacheDataSource = shoppingCartCacheDataSource),
            GetAllItemsUseCase(shoppingCartCacheDataSource = shoppingCartCacheDataSource),
            InsertToCartUseCase(
                shoppingCartCacheDataSource = shoppingCartCacheDataSource
            ),
            UpdateQuantityUseCase(shoppingCartCacheDataSource),
            DeleteNonJncProductsUseCase(shoppingCartCacheDataSource),
            ValidateCouponUseCase(repository),
            ConfirmOrderUseCase(repository),
            ValidateQuantityUseCase(repository)
        )
    }


    @Provides
    @FeatureScope
    fun providesCartAnalyticsImpl(firebaseAnalyticsHelper: FirebaseAnalyticsHelper): CartAnalyticsInterface {
        return CartAnalyticsImpl(firebaseAnalyticsHelper)
    }

    @Provides
    @FeatureScope
    fun providesAgentsOnMapUseCase(repository: CartRepository): AgentsOnMapUseCase {
        return AgentsOnMapUseCase(repository)
    }

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
        categoryEntityService = categoryEntityService)

    @Provides
    @FeatureScope
    fun provideStockService(boxService: BoxStoreService): StockService =
        StockService(boxService)

}
