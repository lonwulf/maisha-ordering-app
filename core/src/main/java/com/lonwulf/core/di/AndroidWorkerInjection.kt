package com.lonwulf.core.di

import androidx.work.CoroutineWorker
import com.lonwulf.core.workers.SyncWorker
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.Multibinds

interface HasWorkerInjector {

    fun workerInjector(): AndroidInjector<CoroutineWorker>
}

@Module
abstract class AndroidWorkerInjectionModule {

    @Multibinds
    abstract fun workerInjectorFactories(): Map<Class<out CoroutineWorker>, AndroidInjector.Factory<out CoroutineWorker>>

    @ContributesAndroidInjector
    abstract fun bindSyncWorker(): SyncWorker
}
