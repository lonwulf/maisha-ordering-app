package com.lonwulf.home.usecase

import com.lonwulf.core.data.mappers.mapSectionDataToDomain
import com.lonwulf.core.domain.models.Section
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
class QueryHomePageSectionsUseCase @Inject constructor(private val repository: HomePageRepository) {
    suspend operator fun invoke(): Flow<List<Section>> = flow {
        val sectionAndData = repository
            .findAllSectionsAndSectionDataItems()

        val sectionsItems = sectionAndData
            .entries
            .map { it.mapSectionDataToDomain(repository.getSectionProductsByIds(it.value)) }
            .toList()
            .sortedBy { it.priority }

        emit(sectionsItems)
    }
}
