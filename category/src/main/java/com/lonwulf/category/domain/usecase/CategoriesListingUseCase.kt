package com.lonwulf.category.domain.usecase

import com.lonwulf.core.domain.models.Category
import com.lonwulf.core.domain.models.CategoryItems
import com.lonwulf.core.utils.Constant.PARENT_CATEGORY_ID
import com.lonwulf.core.utils.GenericApiResult
import com.lonwulf.category.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Get list of categories and related subcategories to use within the app display screen
 * For SubCategories pick first six items sorted by priority
 *
 * IDEA: invoke ensures this is called with no need of a method name
 */
class CategoriesListingUseCase @Inject constructor(private val repository: CategoryRepository) {

    suspend operator fun invoke(): Flow<List<CategoryItems>> = flow {
        val updatedCategoryItems = mutableListOf<CategoryItems>()
        val allCategories = repository.findAllCategories()

        val parentCategoryData = allCategories
            .filter { it.parentId == PARENT_CATEGORY_ID }
            .sortedBy { it.priority }

        parentCategoryData.map { parentCategory ->
            val categoryItems = allCategories
                .filter { it.parentId == parentCategory.id }
                .sortedBy { it.priority }

            updatedCategoryItems.add(
                CategoryItems(parentCategory, categoryItems)
            )
        }

        emit(updatedCategoryItems)
    }

    fun getAllSubCategories(parentId: Int): Flow<GenericApiResult<List<Category>>> = flow {
        val categoryList = repository
            .getSubCategoryByParentId(parentId)
            .sortedBy { it.priority }
        if (categoryList.isNotEmpty()) {
            val result =
                GenericApiResult(results = categoryList, success = true, message = "success")
            emit(result)
        } else {
            val result = GenericApiResult(
                results = emptyList<Category>(),
                success = false,
                message = "error fetching subcategories"
            )
            emit(result)
        }
    }

}
