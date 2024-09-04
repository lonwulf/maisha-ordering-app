package com.lonwulf.home.usecase

import com.lonwulf.core.domain.models.SectionData
import com.lonwulf.home.domain.repositories.HomePageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QuerySectionDataByIdUseCase @Inject constructor(private val repository: HomePageRepository) {

    suspend operator fun invoke(sectionId: Int): Flow<SectionData?> = flow {
        val sectionsItems = repository.getSectionDataById(sectionId)
        emit(sectionsItems)
    }
}
