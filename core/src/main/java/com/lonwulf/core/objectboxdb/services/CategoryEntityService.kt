package com.lonwulf.core.objectboxdb.services

import com.lonwulf.core.objectboxdb.entity.CategoryEntityOB
import com.lonwulf.core.objectboxdb.entity.CategoryEntityOB_
import com.lonwulf.core.objectboxdb.store
import javax.inject.Inject

class CategoryEntityService @Inject constructor(private val boxService: BoxStoreService) {

    private val categoryBox = boxService.getBoxStoreBoxModel(CategoryEntityOB::class.java)

    fun findAll(): List<CategoryEntityOB> {
        return categoryBox.all
    }

    fun insertAll(categories: List<CategoryEntityOB>?) {
        categories?.let { boxService.store(it) }
    }

    fun getCategoryByCategoryId(categoryId: Int): CategoryEntityOB? {
        return categoryBox
            .query(CategoryEntityOB_.categoryId.equal(categoryId))
            .build().use { it.findUnique() }
    }

    fun getCategoryIdByCategoryName(categoryName: String): Int {
        return categoryBox
            .query(CategoryEntityOB_.name.equal(categoryName))
            .build().property(CategoryEntityOB_.categoryId).findInt()
    }

    fun getSubCategoryByParentId(parentId: Int = 2): List<CategoryEntityOB> {
        return categoryBox
            .query(CategoryEntityOB_.parentId.equal(parentId))
            .order(CategoryEntityOB_.priority)
            .build().use { it.find() }
    }

    fun getSubCategoryById(subCategoryId: Int): List<CategoryEntityOB> {
        return categoryBox
            .query(CategoryEntityOB_.categoryId.equal(subCategoryId))
            .order(CategoryEntityOB_.priority)
            .build().use { it.find() }
    }

    fun deleteAll() {
        categoryBox.removeAll()
    }
}
