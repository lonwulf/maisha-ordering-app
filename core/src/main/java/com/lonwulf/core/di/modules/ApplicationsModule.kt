package com.lonwulf.core.di.modules

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationsModule {
    @Provides
    @Singleton
    fun provideFirebaseRecommendLog(): FirebaseRecommendLog {
        return FirebaseRecommendLog()
    }
}
