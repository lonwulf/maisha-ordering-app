package com.lonwulf.home.ui.viewModel

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.models.Product
import com.lonwulf.category.domain.usecase.CartItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val cartItemUseCase: CartItemUseCase,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {
    private val _singleProductItemState = MutableLiveData<ProductItemState>()
    private val _addedToCart = MutableLiveData<AddToCartState>()
    val addedToCart: LiveData<AddToCartState> = _addedToCart

    /**
     * Used sealed class to represent various app states.
     * No need for Enum since on success we need to tie categories list
     */
    sealed class ProductState {
        object Loading : ProductState()
        data class Success(var products: List<Product>) : ProductState()
        object Empty : ProductState()
        data class Error(val errorMsg: String) : ProductState()
    }

    sealed class ProductItemState {
        object Loading : ProductItemState()
        data class Success(val productItem: Product) : ProductItemState()
        object Empty : ProductItemState()
    }

    sealed class AddToCartState {
        object Loading : AddToCartState()
        data class Success(val status: Boolean) : AddToCartState()
        data class Failure(val status: Boolean) : AddToCartState()
    }

    // insert product to cart
    @ExperimentalCoroutinesApi
    fun insertProductToCart(productItem: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            cartItemUseCase(productItem)
                .onStart { setItemAddedToCartResult(AddToCartState.Loading) }
                .flowOn(dispatcherProvider.io)
                .collect {
                    cartAnalyticsInterface.addProductToCart(productItem)
                    setItemAddedToCartResult(AddToCartState.Success(true))
                    // updateCartBadgeCount()
                    addInsiderProductToCart(productItem)
                    facebookEventsAnalyticsInterface.addToCartEvent(
                        bundleOf(
                            "fb_content_id" to productItem.name,
                            "fb_content_type" to productItem.code
                        )
                    )
                }
        }
    }

    fun increaseProductItemQuantity(product: Product?) {
        product?.let { _ ->
            product.quantity = product.quantity.plus(1)
            setProductItemResult(ProductItemState.Success(product))
        }
    }

    fun decreaseProductItemQuantity(product: Product) {
        if (product.quantity >= 1) {
            product.quantity = product.quantity.minus(1)
            setProductItemResult(ProductItemState.Success(product))
        }
    }

    // for single product item.
    private fun setProductItemResult(result: ProductItemState) =
        _singleProductItemState.postValue(result)

    private fun setItemAddedToCartResult(result: AddToCartState) = _addedToCart.postValue(result)
}
