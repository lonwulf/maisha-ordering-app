package com.lonwulf.cart.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lonwulf.core.di.ViewModelFactory
import com.lonwulf.core.di.ViewModelKey
import com.lonwulf.cart.ui.viewModel.CartViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @FlowPreview
    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(CartViewModel::class)
    abstract fun providesCartViewModel(viewModel: CartViewModel): ViewModel

}
