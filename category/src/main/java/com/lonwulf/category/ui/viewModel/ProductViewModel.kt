package com.lonwulf.category.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import com.lonwulf.category.domain.repositories.CategoryRepository
import com.lonwulf.category.domain.usecase.CartItemUseCase
import com.lonwulf.category.domain.usecase.FavoriteProductUseCase
import com.lonwulf.category.domain.usecase.ProductTmplUseCase
import com.lonwulf.category.domain.usecase.QueryProductsByCategoryIdSortedByPriceUseCase
import com.lonwulf.category.domain.usecase.QueryProductsByCategoryIdUseCase
import com.lonwulf.category.domain.usecase.QueryProductsByCodeUseCase
import com.lonwulf.category.domain.usecase.QueryProductsBySectionDataIdUseCase
import com.lonwulf.category.domain.usecase.QueryProductsOrderedByJncUseCase
import com.lonwulf.category.domain.usecase.UpdateProductListingUseCase
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.state.GenericApiResultState
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.SingleLiveData
import com.lonwulf.core.utils.mapSavings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.set

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val favoriteProductUseCase: FavoriteProductUseCase,
    private val getCartUseCase: CartItemUseCase,
    private val tmplProducts: ProductTmplUseCase,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val updateProductListingUseCase: UpdateProductListingUseCase,
    private val categoryRepository: CategoryRepository,
    private val queryProductsByCodeUseCase: QueryProductsByCodeUseCase,
    private val queryProductsBySectionDataIdUseCase: QueryProductsBySectionDataIdUseCase,
    private val queryProductsByCategoryIdUseCase: QueryProductsByCategoryIdUseCase,
    private val queryProductsOrderedByJncUseCase: QueryProductsOrderedByJncUseCase,
    private val queryProductsByCategoryIdSortedByPriceUseCase: QueryProductsByCategoryIdSortedByPriceUseCase
) : ViewModel() {
    // Rep listing of products
    var query: String = ""

    // Rep a single product item
    private val _productItemState = MutableLiveData<ProductItemState>()
    val productItemState: LiveData<ProductItemState> = _productItemState

    private val _addedToCart = MutableLiveData<GenericApiResultState>()
    val addedToCart: LiveData<GenericApiResultState> = _addedToCart

    // Rep a single product item
    private val _updateProduct = MutableLiveData<Product?>()
    val updateProduct: LiveData<Product?> = _updateProduct

    private var _showToast: SingleLiveData<Boolean> = SingleLiveData()
    var showToast: LiveData<Boolean> = _showToast
    var ignoreFavoriteAction = false
    private var product: Product? = null
    var productsDataList: MutableLiveData<List<Product>> = MutableLiveData()
    private val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private var productIdsArray = mutableListOf<Int>()


    private val currentDate = sdf.parse(
        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
            Date()
        )
    )

    sealed class ProductItemState {
        data object Loading : ProductItemState()
        data class Success(val productItem: Product) : ProductItemState()
        data object Empty : ProductItemState()
    }

    @ExperimentalCoroutinesApi
    fun getProducts(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            queryProductsByCategoryIdUseCase(categoryId).flowOn(dispatcherProvider.io)
                .map { productsData ->
                    productsData.map { setJnCData(it) }
                        .also { updateProductListingUseCase(false, productIdsArray) }
                }.collect {
                    productsDataList.postValue(it)
                }
        }
    }

    fun getProductsByCodes(codes: ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            queryProductsByCodeUseCase(codes).flowOn(dispatcherProvider.io).map { productsData ->
                    productsData.map { setJnCData(it) }
                        .also { updateProductListingUseCase(false, productIdsArray) }
                }.collect {
                    productsDataList.postValue(it)
                }
        }
    }

    fun setCurrentProduct(product: Product) {
        this.product = product
    }

    fun getAllTmplProduct(tmplId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tmplProducts(tmplId).flowOn(dispatcherProvider.io)
                .collect { withContext(Dispatchers.Main) { fetchAllProductWithSameTMPLId(it) } }
        }
    }

    private fun fetchAllProductWithSameTMPLId(data: List<Product>) {
        val keyList = ArrayList<String>()
        val keyValuePair = HashMap<String, ArrayList<Attributes>>()
        val selectionPair = HashMap<String, Attributes>()
        val attrList = ArrayList<Attributes>()
        data.forEachIndexed { _, product ->
            val attributes = product.attributes
            attributes?.forEachIndexed { _, attributes ->
                if (!keyList.contains(attributes.name)) keyList.add(attributes.name ?: "")
                attrList.add(attributes)
            }
        }

        keyList.forEachIndexed { _, key ->
            val attributesArray = ArrayList<Attributes>()
            attrList.forEachIndexed { _, attributes ->
                if (attributes.name.equals(key) && attributesArray.none { it.value == attributes.value }) {
                    attributesArray.add(attributes)
                }
            }
            attributesArray.sortBy { it.value }
            keyValuePair[key] = attributesArray
        }

        product?.attributes?.forEachIndexed { _, attributes ->
            keyList.forEachIndexed { _, s ->
                if (attributes.name == s) {
                    selectionPair[s] = attributes
                }
            }
        }


//        attributeAdapter?.setListener(object : AttributeMainAdapter.AttributeListClickListener {
//            override fun onAttributeSelection(attributes: Attributes) {
//                var selectedProduct: Product? = null
//                val selectedProductAttributePair = HashMap<String, Attributes>()
//                for (product in data) {
//                    if (product.attributes?.contains(attributes) == true) {
//                        selectedProduct = product
//                        break
//                    }
//                }
//                selectedProduct?.attributes?.forEachIndexed { _, productAttribute ->
//                    keyList.forEachIndexed { _, s ->
//                        if (productAttribute.name == s) {
//                            selectedProductAttributePair[s] = productAttribute
//                        }
//                    }
//                }
//                _updateProduct.value = selectedProduct
//            }
//        })
    }

    fun quantityTracker(product: Product) {
        ignoreFavoriteAction = true
        getCartUseCase.updateItemQuantity(product.code, product.quantity)
    }

    @ExperimentalCoroutinesApi
    fun addToBasket(product: Product) {
        ignoreFavoriteAction = false
        viewModelScope.launch(Dispatchers.IO) {
            getCartUseCase(product).onStart { setItemAddedToCartResult(GenericApiResultState.Loading) }
                .flowOn(dispatcherProvider.io).collect {
                    setItemAddedToCartResult(GenericApiResultState.Success(true))
                }
        }
        Timber.e("Adding to basket".plus(product.quantity.toString()))
    }

    /**
     * Handler for product favourite, update item in db then update list item
     *
     * @param product product to update
     * @param isList Represent grid and List view so we return List of objects
     */
    fun favouriteProduct(product: Product, isList: Boolean = false) {
        ignoreFavoriteAction = false
        viewModelScope.launch(Dispatchers.IO) {
            product.favorite = product.favorite.not()
            favoriteProductUseCase(product).flowOn(dispatcherProvider.io).collect {
                    withContext(Dispatchers.Main) {
                        _showToast.value = false
                        setProductItemResult(ProductItemState.Success(product))
                    }
                }
        }
    }

    /**
     * Query for products sorted by a given sort type
     *
     * @param categoryId used to fetch for products in a given category
     * @param sortType Constant.SortTypes
     * @param sectionId  If section id != 0  then we query for sorted section items otherwise
     *                   its normal for all products in a given category
     */
    fun sortProductsBySortType(
        categoryId: Int, sortType: String, sectionId: Int, pseudoCategoryName: String = ""
    ) {
        when (sortType) {
            Constant.SortTypes.ASCENDING -> {
                if (pseudoCategoryName.isNotEmpty()) {
                    pseudoCatProductsPriceSort(
                        pseudoCategoryName, isAscending = Constant.SortTypes.Values.ASCENDING
                    )
                } else if (sectionId == 0) {
                    productsPriceSort(categoryId, Constant.SortTypes.Values.ASCENDING)
                } else {
                    querySectionDataProducts(sectionId, Constant.SortTypes.Values.ASCENDING)
                }
            }

            Constant.SortTypes.DESCENDING -> {
                if (pseudoCategoryName.isNotEmpty()) {
                    pseudoCatProductsPriceSort(
                        pseudoCategoryName, isAscending = Constant.SortTypes.Values.DESCENDING
                    )
                } else if (sectionId == 0) {
                    productsPriceSort(categoryId, Constant.SortTypes.Values.DESCENDING)
                } else {
                    querySectionDataProducts(sectionId, Constant.SortTypes.Values.DESCENDING)
                }
            }

            Constant.SortTypes.JISTOCK -> {
                viewModelScope.launch(Dispatchers.IO) {
                    queryProductsOrderedByJncUseCase(categoryId).flowOn(dispatcherProvider.io)
                        .map { productsData ->
                            productsData.map { setJnCData(it) }
                                .also { updateProductListingUseCase(false, productIdsArray) }
                        }.collect {
                            productsDataList.postValue(it)
                        }
                }
            }
        }
    }

    fun querySectionDataProducts(
        sectionDataId: Int, isAscending: Int? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            queryProductsBySectionDataIdUseCase(sectionDataId).flowOn(dispatcherProvider.io)
                .map { productsData ->
                    productsData.map { setJnCData(it) }
                        .also { updateProductListingUseCase(false, productIdsArray) }
                }.collect { productList ->
                    when (isAscending) {
                        1 -> productList.sortedByDescending { it.price }
                        0 -> productList.sortedBy { it.price }
                    }
                    productsDataList.postValue(productList)
                }
        }
    }

    private fun pseudoCatProductsPriceSort(pseudoCategoryName: String, isAscending: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            queryProductsByPseudoCatNameSortedByPriceUseCase(
                pseudoCategoryName,
                isAscending
            ).flowOn(dispatcherProvider.io).map { productsData ->
                    productsData.map { setJnCData(it) }
                        .also { updateProductListingUseCase(isJNC = false, productIdsArray) }
                }.collect { productsDataList.postValue(it) }
        }
    }

    private fun productsPriceSort(
        categoryId: Int, isAscending: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            queryProductsByCategoryIdSortedByPriceUseCase(categoryId, isAscending).flowOn(
                    dispatcherProvider.io
                ).map { productsData ->
                    productsData.map { setJnCData(it) }
                        .also { updateProductListingUseCase(false, productIdsArray) }
                }.collect {
                    productsDataList.postValue(it)
                }
        }
    }

    private fun setJnCData(product: Product): Product {
        if (dateFromJNC.isNotEmpty()) {
            val jncEndDate = sdf.parse(dateFromJNC)
            val isDayPassed = jncEndDate?.compareTo(currentDate)
            isDayPassed?.let {
                if (it > 0 && product.isProductJnc) {
                    product.isProductJnc = false
                    productIdsArray.add(product.id.toInt())
                }
            }
        }
        return product
    }

    private fun pagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            prefetchDistance = 3 * 10,
            initialLoadSize = 2 * 10
        )
    }

    private fun pagingForRecommendationsConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 6, enablePlaceholders = false, maxSize = 300, prefetchDistance = 10
        )
    }

    // for single product item.
    private fun setProductItemResult(result: ProductItemState) = _productItemState.postValue(result)

    private fun setItemAddedToCartResult(result: GenericApiResultState) =
        _addedToCart.postValue(result)

    fun getSavings(savings: Double): CharSequence = savings.mapSavings()

}
