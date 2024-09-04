package com.lonwulf.home.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.home.domain.repositories.HomePageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchProductsByCodeUseCase @Inject constructor(private val repository: HomePageRepository) {
    suspend operator fun invoke(productCodesArray: Array<String>): Flow<List<Product>> = flow {
        val productItems = mutableListOf<Product>()
        if (productCodesArray.isNotEmpty()) {
            val items = repository.getProductsWithProductCodes(productCodesArray)
            productItems.addAll(items)
            emit(productItems)
        } else {
            emit(productItems)
        }
    }
}
