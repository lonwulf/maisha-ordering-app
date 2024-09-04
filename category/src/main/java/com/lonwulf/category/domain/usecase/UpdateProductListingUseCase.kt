package com.lonwulf.category.domain.usecase

import com.lonwulf.category.domain.repositories.CategoryRepository
import javax.inject.Inject

class UpdateProductListingUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(isJNC: Boolean, productIdsListArray: MutableList<Int>) {
        repository.updateProductsListing(isJNC, productIdsListArray)
    }
}
