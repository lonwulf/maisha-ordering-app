package com.lonwulf.home.di

import com.lonwulf.category.data.CategoryRepositoryImpl
import com.lonwulf.category.domain.repositories.CategoryRepository

import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun bindCategoryRepository(categoryRepository: CategoryRepositoryImpl): CategoryRepository
}
