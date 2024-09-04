package com.lonwulf.home.domain

import com.lonwulf.core.domain.models.CategoryItems
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.domain.models.SectionData

interface SectionClickListeners {
    fun onCategoryItemClick(categoryItems: CategoryItems)
    fun onProductItemClick(product: Product)
    fun onBuyNowItemClick(product: Product)
    fun onViewPagerItemClick(sectionData: SectionData)
    fun onSeeAllSectionItemClick(sectionData: SectionData)
}
