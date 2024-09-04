package com.lonwulf.core.di.modules

import android.content.Context
import com.lonwulf.core.objectboxdb.entity.MyObjectBox
import com.lonwulf.core.objectboxdb.services.BoxStoreService
import com.lonwulf.core.objectboxdb.services.BoxStoreServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ObjectBoxModule {
    @Provides
    @Singleton
    @Synchronized
    fun provideBoxStoreDatabase(context: Context): BoxStoreService {
        return BoxStoreServiceImpl(context) { MyObjectBox.builder() }
    }
}
