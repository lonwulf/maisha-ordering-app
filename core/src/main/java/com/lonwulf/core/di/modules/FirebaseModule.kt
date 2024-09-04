package com.lonwulf.core.di.modules

import com.lonwulf.core.analytics.FirebaseAnalyticsHelper
import com.lonwulf.core.repository.RTDBRepository
import com.lonwulf.core.repository.RTDBRepositoryImpl
import com.lonwulf.core.repository.RemoteConfigRepository
import com.lonwulf.core.repository.RemoteConfigRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun remoteConfigRepository(firebaseAnalyticsHelper: FirebaseAnalyticsHelper): RemoteConfigRepository {
        return RemoteConfigRepositoryImpl(firebaseAnalyticsHelper)
    }

    @Provides
    @Singleton
    fun providesRTDBConfig(): RTDBRepository {
        return RTDBRepositoryImpl()
    }
}
