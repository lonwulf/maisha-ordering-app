package com.lonwulf.category.data.source

import com.lonwulf.core.network.RemoteDataSource
import com.lonwulf.category.data.CategoryService

/**
 * Provides access to making api calls by various endpoints for
 * Products
 * @property categoryService: Retrofit instance service with related endpoints
 */
class CategoryRemoteDataSource(private val categoryService: CategoryService) : RemoteDataSource() {

}
