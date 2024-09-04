@file:Suppress("LongParameterList")

package com.lonwulf.category.ui.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lonwulf.category.domain.usecase.FavoriteProductUseCase
import com.lonwulf.category.domain.usecase.SearchProductUseCase
import com.lonwulf.core.data.mappers.mapEntityToDomain
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.objectboxdb.services.StockService
import com.lonwulf.core.utils.SingleLiveData
import com.lonwulf.core.utils.Utils.getOutStockProducts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val context: Context,
    private val searchProductUseCase: SearchProductUseCase,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val favoriteProductUseCase: FavoriteProductUseCase,
    private val stockService: StockService
) : AndroidViewModel(context as Application) {
    private val _productState = MutableLiveData<ProductState>()
    val productState: LiveData<ProductState> get() = _productState

    private val _activeProduct = MutableLiveData<Product>()
    val activeProduct: LiveData<Product> get() = _activeProduct

    private val _buttonActions: SingleLiveData<String> = SingleLiveData()
    val buttonActions: LiveData<String> get() = _buttonActions

    private lateinit var productListItems: MutableList<Product>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            setProductListResult(ProductState.Loading)
            searchProductUseCase().flowOn(dispatcherProvider.io).collect { productList ->
                    withContext(Dispatchers.Main) {
                        // remove out of stock items from search list
                        val stockInfo = stockService.findAll().map {
                            it.mapEntityToDomain()
                        }

                        val outOfStockProducts =
                            getOutStockProducts(products = productList, stockInfoList = stockInfo)

                        productListItems = productList.toMutableList()
                        productListItems.removeAll(outOfStockProducts)
                        // filter out oos products currently in db
                        // sync might not have happened and outOfStockProducts is empty
                        productListItems =
                            productListItems.filter { !it.isOutOfStock() }.toMutableList()
                        setProductListResult(ProductState.DoneLoading)
                    }
                }
        }
    }

    private fun setProductListResult(result: ProductState) = _productState.postValue(result)

    sealed class ProductState {
        object Loading : ProductState()
        object Searching : ProductState()
        object DoneLoading : ProductState()
        object Success : ProductState()
        object Empty : ProductState()
    }

    fun filterProducts(searchQuery: String) {
        setProductListResult(ProductState.Searching)
        if (::productListItems.isInitialized) {
            val searchResults = productListItems.filter {
                it.searchTerms?.lowercase(Locale.getDefault())
                    ?.contains(searchQuery) == true || (it.code.lowercase(Locale.getDefault())
                    ?.contains(searchQuery)
                    ?: "") == true || it.name.lowercase(Locale.getDefault())
                    ?.contains(searchQuery) == true
            }
            if (searchResults.isNotEmpty()) {
                setProductListResult(ProductState.Success)
            } else {
                setProductListResult(ProductState.Empty)
            }
        } else {
            setProductListResult(ProductState.Loading)
        }
    }

    fun favouriteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteProductUseCase(product).flowOn(dispatcherProvider.default)
                .collect { productItem ->
                    withContext(Dispatchers.Main) {
                        productListItems.mapIndexed { index, product ->
                            if (product.code.equals(productItem.code, true)) {
                                productListItems[index] = productItem
                            }
                        }
                    }
                    setProductListResult(ProductState.Success)
                }
        }
    }
}
