@file:Suppress("TooManyFunctions")

package com.lonwulf.category.di

import android.content.Context
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
import com.lonwulf.core.di.scope.FeatureScope
import com.lonwulf.core.objectboxdb.services.BoxStoreService
import com.lonwulf.core.objectboxdb.services.CartCacheService
import com.lonwulf.core.objectboxdb.services.CategoryEntityService
import com.lonwulf.core.objectboxdb.services.ProductService
import com.lonwulf.core.objectboxdb.services.SectionDataService
import com.lonwulf.category.data.CategoryService
import com.lonwulf.category.data.source.CategoryRemoteDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ProductModule {

    @Provides
    @FeatureScope
    fun providesCategoryEntityService(boxService: BoxStoreService): CategoryEntityService {
        return CategoryEntityService(boxService)
    }

    @Provides
    @FeatureScope
    fun providesProductService(boxService: BoxStoreService): ProductService =
        ProductService(boxService)

    @Provides
    @FeatureScope
    fun providesCategoryService(retrofit: Retrofit): CategoryService = retrofit.create(
        CategoryService::class.java
    )

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
}
