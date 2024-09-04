package com.lonwulf.core.di.modules

import android.content.Context
import com.lonwulf.core.analytics.FirebaseAnalyticsHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseAnalyticsModule {

    @Provides
    @Singleton
    fun provideFirebaseAnalyticsHelper(context: Context): FirebaseAnalyticsHelper {
        return FirebaseAnalyticsHelper(context)
    }
}
