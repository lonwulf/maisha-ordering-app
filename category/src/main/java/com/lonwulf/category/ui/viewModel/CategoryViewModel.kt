package com.lonwulf.category.ui.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lonwulf.category.domain.entities.SubCategory
import com.lonwulf.category.domain.usecase.CartItemUseCase
import com.lonwulf.category.domain.usecase.CategoriesListingUseCase
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.models.Category
import com.lonwulf.core.domain.models.CategoryItems
import com.lonwulf.core.domain.usecase.FetchCategoriesUseCase
import com.lonwulf.core.state.GenericApiResultState
import com.lonwulf.core.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesListingUseCase: CategoriesListingUseCase,
    private val context: Context,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val getCartUseCase: CartItemUseCase,
    private val fetchCategoriesUseCase: FetchCategoriesUseCase
) : AndroidViewModel(context as Application) {
    private val _subCategoryItemClickState = SingleLiveData<Category>()
    private var _categoryItems = listOf<CategoryItems>()


    val subCategoryClickState: LiveData<Category> = _subCategoryItemClickState
    private var activeCategoryId = 0
    private var categoryItemsList: MutableList<CategoryItems> = mutableListOf()
    var categoriesGenericState = SingleLiveData<GenericApiResultState>()

    private fun getAppCategories(categoryItems: List<CategoryItems>): MutableMap<Category, SubCategory> {
        val mapOfParentAndSubCategory = mutableMapOf<Category, SubCategory>()
        categoryItems.map {
            val subCategory = SubCategory(
                it.parentCategory.id, it.parentCategory.name, it.subCategory
            )
            mapOfParentAndSubCategory.put(it.parentCategory, subCategory)
        }

        return mapOfParentAndSubCategory
    }

    private fun setResult(result: GenericApiResultState) = categoriesGenericState.postValue(result)

    /**
     * Fetch categories from db if empty do fetch from API
     *
     */
    fun getCategories() {
        viewModelScope.launch(dispatcherProvider.io) {
            getCategoriesListingUseCase.invoke()
                .onStart { setResult(GenericApiResultState.Loading) }
                .flowOn(dispatcherProvider.default).collect { categoryItems ->
                    if (categoryItems.isEmpty()) {
                        fetchCategoriesUseCase().flowOn(dispatcherProvider.default)
                            .collect { genericApiResult ->
                                if (genericApiResult.success) {
                                    categoryItemsList = genericApiResult.results.toMutableList()
                                    if (categoryItemsList.isEmpty()) {
                                        setResult(GenericApiResultState.Empty)
                                    } else {
                                        setCategoryData(categoryItemsList)
                                    }
                                } else {
                                    setResult(GenericApiResultState.Error(genericApiResult.message))
                                }
                            }
                    } else {
                        setCategoryData(categoryItems)
                    }
                }
        }
    }

    private fun setCategoryData(categoryItemsList: List<CategoryItems>) {
        _categoryItems = categoryItemsList
        setResult(GenericApiResultState.Success(true))

        val parentAndSubCategoryData = getAppCategories(categoryItemsList)
        val activeParentCategory = getActiveParentCategoryByParentCategoryId(
            parentAndSubCategoryData, activeCategoryId
        )
        val activeParentSubCategoryData = parentAndSubCategoryData[activeParentCategory]

        viewModelScope.launch(dispatcherProvider.ui) {
            setParentCategoryList(parentAndSubCategoryData.keys.toMutableList())
        }

        activeParentSubCategoryData?.let {
            activeCategoryId = it.id

            viewModelScope.launch(dispatcherProvider.ui) {
                setSubCategoryList(it.subCategoryListItems)
            }
        }
    }

    private fun getActiveParentCategoryByParentCategoryId(
        shopCategoriesData: MutableMap<Category, SubCategory>, parentCategoryId: Int
    ): Category {
        return if (parentCategoryId == 0) {
            shopCategoriesData.keys.first()
        } else {
            shopCategoriesData.keys.first { it.id == parentCategoryId }
        }
    }

    private fun mainCategoryClicked(parentCategory: Category) {
        activeCategoryId = parentCategory.id
        return if (_categoryItems.isNotEmpty()) {
            setCategoryData(_categoryItems)
        } else {
            getCategories()
        }
    }

    fun onParentCategoryItemClick(category: Category, position: Int) {
        mainCategoryClicked(category)
    }

    fun onChildCategoryItemClick(category: Category, position: Int) {
        _subCategoryItemClickState.value = category
    }

    private fun setParentCategoryList(data: MutableList<Category>) {
    }

    private fun setSubCategoryList(data: List<Category>) {
    }
}
