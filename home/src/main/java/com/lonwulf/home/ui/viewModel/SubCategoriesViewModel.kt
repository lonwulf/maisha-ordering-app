package com.lonwulf.home.presentation.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lonwulf.core.analytics.CategoryAnalyticsInterface
import com.lonwulf.core.analytics.FirebaseAnalyticsHelper
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.models.Category
import com.lonwulf.core.state.GenericApiResultState
import com.lonwulf.core.utils.FirebaseRecommendLog
import com.lonwulf.core.utils.SingleLiveData
import com.lonwulf.home.R
import com.lonwulf.home.presentation.adapter.SubCategoryItemAdapter
import com.lonwulf.home.usecase.QuerySubcategoryByCategoryIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubCategoriesViewModel @Inject constructor(
    val firebaseRecommendLog: FirebaseRecommendLog,
    private val context: Context,
    private val querySubcategoryByCategoryIdUseCase: QuerySubcategoryByCategoryIdUseCase,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    internal val firebaseAnalyticsHelper: FirebaseAnalyticsHelper,
    internal val categoryAnalyticsInterface: CategoryAnalyticsInterface
) : AndroidViewModel(context as Application), SubCategoryItemAdapter.SubCategoryClickListener {
    private val subCategoryState: MutableLiveData<GenericApiResultState> = MutableLiveData()
    val subCategoryItemAdapter =
        SubCategoryItemAdapter(R.layout.home_single_item_sub_category, this, context, this)
    private var _subCategoryMutableLiveDataClick: SingleLiveData<Category> = SingleLiveData()
    val subCategoryMutableLiveDataClick: LiveData<Category>
        get() = _subCategoryMutableLiveDataClick

    fun fetchSubCategories(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            querySubcategoryByCategoryIdUseCase.invoke(categoryId)
                .onStart { setResult(GenericApiResultState.Loading) }
                .flowOn(dispatcherProvider.io)
                .collect { subCategoryItems ->
                    withContext(Dispatchers.Main) {
                        if (subCategoryItems.isEmpty()) {
                            setResult(GenericApiResultState.Empty)
                        } else {
                            setResult(GenericApiResultState.Success(true))
                            subCategoryItemAdapter.submitList(subCategoryItems)
                        }
                    }
                }
        }
    }

    private fun setResult(result: GenericApiResultState) =
        subCategoryState.postValue(result)

    override fun onSubCategoryItemClick(category: Category) {
        firebaseRecommendLog.logSubCategoryRecommendation(category.id, category.id, category.name)
        _subCategoryMutableLiveDataClick.value = category
        categoryAnalyticsInterface.viewProductList("subcategory")
        categoryAnalyticsInterface.viewProductList(
            categoryId = category.id.toString(),
            categoryName = category.displayName.toString()
        )
    }
}
