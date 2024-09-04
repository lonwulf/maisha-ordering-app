package com.lonwulf.core.di.modules

import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.dispatcher.DispatcherProvider
import dagger.Binds
import dagger.Module

@Module
interface CoroutineDispatcherModule {
    @Binds
    fun bindDispatcher(dispatcherProvider: CoroutineDispatcherProvider): DispatcherProvider
}
