package com.lonwulf.home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lonwulf.core.di.ViewModelFactory
import com.lonwulf.core.di.ViewModelKey
import com.lonwulf.home.ui.viewModel.HomeViewModel
import com.lonwulf.home.ui.viewModel.ProductViewModel
import com.lonwulf.home.ui.viewModel.SubCategoriesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


}
