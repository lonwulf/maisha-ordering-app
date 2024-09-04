package com.lonwulf.home.usecase

import com.lonwulf.core.domain.models.CategoryItems
import com.lonwulf.core.domain.models.PseudoCategory
import com.lonwulf.home.domain.repositories.HomePageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Get list of categories and related subcategories to use within the app display screen
 * For SubCategories pick first six items sorted by priority
 *
 * IDEA: invoke ensures this is called with no need of a method name
 */
class QueryCategoriesUseCase @Inject constructor(private val repository: HomePageRepository) {

    suspend operator fun invoke(): Flow<List<CategoryItems>> = flow {
        val categoryItems = mutableListOf<CategoryItems>()
        val categories =
            repository.getParentCategories()
                .sortedBy { it.priority }
        for (category in categories) {
            categoryItems.add(CategoryItems(category, ArrayList()))
        }
        emit(categoryItems)
    }

    suspend fun getPseudoCatsWithProducts(): Flow<List<PseudoCategory>> = flow {
        emit(repository.getPseudoCategoriesWithProductsFromDb().sortedBy { it.name })
    }

    suspend fun localDbHasPseudos(): Flow<Boolean> = flow {
        emit(repository.localDbHasPseudos())
    }

    suspend fun updatePseudoProducts(): Flow<List<PseudoCategory>> = flow {
        emit(repository.updatePseudoProducts())
    }
}
