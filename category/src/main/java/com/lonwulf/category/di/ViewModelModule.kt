package com.lonwulf.category.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lonwulf.core.di.ViewModelFactory
import com.lonwulf.core.di.ViewModelKey
import com.lonwulf.category.ui.viewModel.CategoryViewModel
import com.lonwulf.category.ui.viewModel.ProductViewModel
import com.lonwulf.category.ui.viewModel.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun providesCategoryViewModel(viewModel: CategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    abstract fun providesProductViewModel(viewModel: ProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun providesSearchViewModel(viewModel: SearchViewModel): ViewModel
}
