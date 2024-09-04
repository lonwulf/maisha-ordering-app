package com.lonwulf.home.usecase

import com.lonwulf.core.domain.models.SectionData
import com.lonwulf.home.domain.repositories.HomePageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SectionDataListingUseCase @Inject constructor(private val repository: HomePageRepository) {

    private suspend operator fun invoke(sectionId: Int): Flow<List<SectionData>> = flow {
        val sectionsItems = repository.getSectionDataBySectionId(sectionId)
            .sortedBy { it.priority }
        emit(sectionsItems)
    }
}
