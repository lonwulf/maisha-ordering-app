package com.lonwulf.home.usecase

import com.lonwulf.core.domain.models.Category
import com.lonwulf.home.domain.repositories.HomePageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Get list of sub categories and to use within the app display screen*
 * IDEA: invoke ensures this is called with no need of a method name
 */
class QuerySubcategoryByCategoryIdUseCase @Inject constructor(private val repository: HomePageRepository) {

    suspend operator fun invoke(categoryId: Int): Flow<List<Category>> = flow {
        val subCategoryItems = mutableListOf<Category>()
        subCategoryItems.addAll(repository.getSubCategoryByParentId(categoryId))
        emit(subCategoryItems)
    }
}
