package com.lonwulf.category.di

import com.lonwulf.core.di.CoreComponent
import com.lonwulf.core.di.scope.FeatureScope
import com.lonwulf.category.presentation.screen.CategoryFragment
import com.lonwulf.category.presentation.screen.product.ProductItemBottomSheetFragment
import com.lonwulf.category.presentation.screen.product.ProductItemFragment
import com.lonwulf.category.presentation.screen.product.ProductListFragment
import com.lonwulf.category.presentation.screen.product.ProductSuccessBottomSheetFragment
import com.lonwulf.category.presentation.screen.search.SearchFragment
import dagger.Component

@FeatureScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [
        CategoryModule::class,
        ViewModelModule::class,
        RepositoryModule::class,
    ]
)
interface CategoryComponent {
//    fun inject(categoryFragment: CategoryFragment)
//    fun inject(searchFragment: SearchFragment)
//    fun inject(productListFragment: ProductListFragment)
//    fun inject(productItemFragment: ProductItemFragment)
//    fun inject(productItemBottomSheetFragment: ProductItemBottomSheetFragment)
//    fun inject(productSuccessBottomSheetFragment: ProductSuccessBottomSheetFragment)
}
