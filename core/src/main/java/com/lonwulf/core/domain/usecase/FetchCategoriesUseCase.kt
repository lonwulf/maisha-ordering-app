package com.lonwulf.core.domain.usecase

import com.lonwulf.core.domain.models.CategoryItems
import com.lonwulf.core.domain.repository.DataRepository
import com.lonwulf.core.utils.GenericApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Simulate apiFetch and insertion to DB
 *
 * @property repository
 */
class FetchCategoriesUseCase @Inject constructor(private val repository: DataRepository) {

    suspend operator fun invoke(): Flow<GenericApiResult<List<CategoryItems>>> = flow {
        // Fetch and Insert to db then query items to return.
        repository.fetchCategories()

        val categoryItems = mutableListOf<CategoryItems>()

        repository.getParentCategories()
            .sortedBy { it.priority }
            .map {
                val subcategories = repository.getSubCategoryByParentId(it.id)
                    .sortedBy { subCat -> subCat.priority }
                    .take(6)

                categoryItems.add(CategoryItems(it, subcategories))
            }
        if (categoryItems.isNotEmpty()) {
            val genericApiResult =
                GenericApiResult(results = categoryItems, success = true, message = "success")
            emit(genericApiResult)
        } else {
            val genericApiResult = GenericApiResult(
                results = emptyList<CategoryItems>(),
                success = false,
                message = "no categories found"
            )
            emit(genericApiResult)
        }
    }
}
