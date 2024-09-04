package com.lonwulf.home.di

import com.lonwulf.core.di.CoreComponent
import com.lonwulf.core.di.scope.FeatureScope
import com.lonwulf.home.presentation.screen.HomeFragment
import com.lonwulf.home.presentation.screen.SetCustomerLocationMapFragment
import com.lonwulf.home.presentation.screen.SubCategoriesListingFragment
import com.lonwulf.home.presentation.screen.bottom_sheet.HomeProductItemBottomSheetFragment
import com.lonwulf.home.presentation.screen.bottom_sheet.HomeProductSuccessBottomSheetFragment
import dagger.Component

@FeatureScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [
        HomeModule::class,
        ViewModelModule::class,
        RepositoryModule::class
    ]
)
interface HomeComponent {
//    fun inject(homeFragment: HomeFragment)
//    fun inject(subCategoriesListingFragment: SubCategoriesListingFragment)
//    fun inject(homeProductItemBottomSheetFragment: HomeProductItemBottomSheetFragment)
//    fun inject(productSuccessBottomSheetFragment: HomeProductSuccessBottomSheetFragment)
//    fun inject(setCustomerLocationMapFragment: SetCustomerLocationMapFragment)
}
