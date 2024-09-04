package com.lonwulf.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.lonwulf.core.database.EcatDatabase
import com.lonwulf.core.di.modules.ApplicationsModule
import com.lonwulf.core.di.modules.ContextModule
import com.lonwulf.core.di.modules.CoroutineDispatcherModule
import com.lonwulf.core.di.modules.DataModule
import com.lonwulf.core.di.modules.FirebaseAnalyticsModule
import com.lonwulf.core.di.modules.FirebaseModule
import com.lonwulf.core.di.modules.NetworkModule
import com.lonwulf.core.di.modules.ObjectBoxModule
import com.lonwulf.core.di.modules.SharedPreferenceModule
import com.lonwulf.core.dispatcher.DispatcherProvider
import com.lonwulf.core.objectboxdb.services.BoxStoreService
import com.lonwulf.core.workers.SyncWorker
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ContextModule::class,
        NetworkModule::class,
        ObjectBoxModule::class,
        SharedPreferenceModule::class,
        CoroutineDispatcherModule::class,
        ApplicationsModule::class,
        FirebaseModule::class,
        FirebaseAnalyticsModule::class,
        DataModule::class,
        AndroidWorkerInjectionModule::class
    ]
)
interface CoreComponent {
    fun context(): Context
    fun sharedPreference(): SharedPreferences
    fun sharedPreferenceEditor(): SharedPreferences.Editor
    fun retrofit(): Retrofit
    fun dispatcher(): DispatcherProvider
    fun boxStoreService(): BoxStoreService
    fun inject(syncWorker: SyncWorker)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): CoreComponent
    }
}
