package com.lonwulf.core.data.mappers

import com.lonwulf.core.data.response.catalog.SectionDto
import com.lonwulf.core.domain.models.CategoryItems
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.domain.models.PseudoCategory
import com.lonwulf.core.domain.models.Section
import com.lonwulf.core.domain.models.SectionData
import com.lonwulf.core.mapper.mapImageUrl
import com.lonwulf.core.objectboxdb.entity.HomePageEntityOB
import com.lonwulf.core.objectboxdb.entity.SectionDataEntityOB
import com.lonwulf.core.utils.Constant

fun SectionDto.mapApiResponseToHomepageEntity(): Pair<List<HomePageEntityOB>, List<SectionDataEntityOB>> {
    val homePageSections = mutableListOf<HomePageEntityOB>()
    Constant.LAST_SYNC_IMAGE_SIGNATURE = System.currentTimeMillis()
    val sectionDataEntityList = mutableListOf<SectionDataEntityOB>()
    this.homepage_data?.sections?.forEach {
        homePageSections.add(
            HomePageEntityOB(
                sectionId = it.id ?: 0,
                viewType = it.view_type ?: "",
                sectionTitle = it.section_title ?: "",
                priority = it.priority ?: 0,
                active = it.active ?: false
            )
        )
        it.section_data.mapSectionDataDtoToEntity(it.id ?: 0)?.let {
            sectionDataEntityList.addAll(it)
        }
    }
    return Pair(homePageSections, sectionDataEntityList)
}

fun List<SectionDto.HomePageData.Sections.SectionData>?.mapSectionDataDtoToEntity(sectionId: Int): List<SectionDataEntityOB>? {
    return this?.map { sectionDataDto ->
        val productIds = mutableListOf<Int>()
        sectionDataDto.promos?.map { promoDto ->
            promoDto.id?.let { id ->
                productIds.add(id)
            }
        }.also {
            productIds.addAll(sectionDataDto.product_ids ?: emptyList())
        }
        SectionDataEntityOB(
            sectionId = sectionId,
            sectionDataId = sectionDataDto.id ?: 0,
            title = sectionDataDto.title.orEmpty(),
            image = sectionDataDto.image.orEmpty(),
            productIds = productIds,
        )
    }
}

fun mapSectionDataEntityToHomepageEntity(
    allHomePageData: List<HomePageEntityOB>,
    allSectionData: List<SectionDataEntityOB>
): Map<HomePageEntityOB, List<SectionDataEntityOB>> {
    val sectionAndSectionDataItem = hashMapOf<HomePageEntityOB, List<SectionDataEntityOB>>()
    allHomePageData.forEach { homePageData ->
        val sectionDataList = mutableListOf<SectionDataEntityOB>()
        allSectionData.filter { it.sectionId == homePageData.sectionId }.forEach {
            // it.promos = mapPromosToPromosEntity(homePageData.promos)
            sectionDataList.add(it)
        }
        sectionAndSectionDataItem[homePageData] = sectionDataList
    }
    return sectionAndSectionDataItem
}

fun List<SectionDataEntityOB>.mapHomePageUrls(): MutableList<String> {
    val imageUrls: MutableList<String> = mutableListOf()
    for (sectionData in this) {
        sectionData.let {
            if (sectionData.image.isNotEmpty()) {
                imageUrls.add(Constant.getBaseImageUrl() + sectionData.image)
            }
        }
    }
    imageUrls.removeAll(listOf(Constant.getBaseImageUrl()))
    return imageUrls
}

fun SectionDataEntityOB.mapSectionDataToDomain(products: List<Product>): SectionData {
    return SectionData(
        id = this.sectionDataId,
        sectionId = this.sectionId,
        active = true,
        priority = 1,
        title = this.title,
        image = this.image.mapImageUrl(),
        productsData = products.filter {
            this.productIds?.contains(it.id?.toInt()) == true
        },
    )
}

fun Map.Entry<HomePageEntityOB, List<SectionDataEntityOB>>.mapSectionDataToDomain(
    products: List<Product>
): Section {
    val sectionItem = this.key.mapSectionToDomain()
    sectionItem.sectionData = this.value.map {
        it.mapSectionDataToDomain(products)
    }
    return sectionItem
}

fun HomePageEntityOB.mapSectionToDomain(): Section {
    return Section(
        id = this.sectionId,
        active = this.active,
        viewType = this.viewType,
        priority = this.priority,
        sectionTitle = this.sectionTitle,
        sectionData = emptyList(),
        categories = emptyList(),
        popularProducts = emptyList(),
        products = null,
        pseudoCategories = emptyList()
    )
}

fun List<PseudoCategory>.mapPseudoCategoriesToSectionDomain(): Section {
    return Section(
        id = 1,
        active = true,
        viewType = Constant.PSEUDO_CATEGORY,
        priority = 1,
        pseudoCategories = this
    )
}

fun List<CategoryItems>.mapCategoriesSectionToDomain(): Section {
    return Section(
        id = 1,
        active = true,
        viewType = Constant.CATEGORY,
        priority = 1,
        categories = this
    )
}
