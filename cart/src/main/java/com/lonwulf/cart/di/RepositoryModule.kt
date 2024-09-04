package com.lonwulf.cart.di

import com.lonwulf.cart.data.CartRepositoryImpl
import com.lonwulf.cart.domain.repository.CartRepository
import com.lonwulf.feature_loan.data.LoansRepositoryImpl
import com.lonwulf.feature_loan.domain.repositories.LoansRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun bindCartRepository(cartRepository: CartRepositoryImpl): CartRepository

    @Binds
    fun bind(loansRepositoryImpl: LoansRepositoryImpl): LoansRepository
}
