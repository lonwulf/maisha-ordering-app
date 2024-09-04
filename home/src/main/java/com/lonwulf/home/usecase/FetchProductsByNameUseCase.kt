package com.lonwulf.home.usecase

import com.lonwulf.core.domain.models.Product
import com.lonwulf.home.domain.repositories.HomePageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchProductsByNameUseCase @Inject constructor(private val repository: HomePageRepository) {
    suspend operator fun invoke(brandNames: Array<String>): Flow<List<Product>> = flow {
        val productItems = mutableListOf<Product>()
        if (brandNames.isNotEmpty()) {
            val items = repository.getRecommendedProductsByBrandNames(brandNames)
            productItems.addAll(items)
            emit(productItems)
        } else {
            emit(productItems)
        }
    }
}
