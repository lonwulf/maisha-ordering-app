package com.lonwulf.cart.ui.viewModel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lonwulf.cart.domain.usecase.CartListUseCase
import com.lonwulf.core.dispatcher.CoroutineDispatcherProvider
import com.lonwulf.core.domain.models.Agent
import com.lonwulf.core.domain.models.Product
import com.lonwulf.core.domain.models.StockInfo
import com.lonwulf.core.state.GenericApiResultState
import com.lonwulf.core.utils.Constant
import com.lonwulf.core.utils.Prefs
import com.lonwulf.core.utils.SingleLiveData
import com.lonwulf.core.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModel @javax.inject.Inject constructor(
    private val cartListUseCase: CartListUseCase,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {
    var editedPosition: Int = 0
    val grandTotalObservableField=
       MutableStateFlow("0.00")
    val discountObservableField =
       MutableStateFlow("0.00")
    val cartCountObservableField =
       MutableStateFlow("0.00")
    val savingsTotalObservable =
       MutableStateFlow("0.00")
    var cartList = mutableListOf<Product>()
    private var _liveDataCartList: SingleLiveData<List<Product>> =
        com.lonwulf.core.utils.SingleLiveData()
    val liveDataCartList: LiveData<List<Product>>
        get() = _liveDataCartList
    private val _cartState =
        MutableLiveData<GenericApiResultState>()
    val cartState: LiveData<GenericApiResultState> get() = _cartState
    private var _buttonActions: SingleLiveData<String> = SingleLiveData()
    val buttonActions: LiveData<String> get() = _buttonActions
    private val _quantityObservableField =
       MutableStateFlow("")
    private var _cartErrorsMutableLiveData: SingleLiveData<String> = SingleLiveData()
    val cartErrorsMutableLiveData: LiveData<String> get() = _cartErrorsMutableLiveData
    private val _placeOrderState =SingleLiveData<GenericApiResultState>()
    val placeOrderState: LiveData<GenericApiResultState> get() = _placeOrderState
    private var productAtPosition: Product? = null
    val totalObservableField:MutableStateFlow<String> =
       MutableStateFlow("0.00")

    private val _maxCapReached: SingleLiveData<Boolean> =SingleLiveData()
    val maxCapReached: LiveData<Boolean> get() = _maxCapReached

    private val _minCapReached: SingleLiveData<Boolean> =SingleLiveData()
    val minCapReached: LiveData<Boolean> get() = _minCapReached

    var currentProduct: Product? = null
    var previousAgents = mutableListOf<Agent>()

    private var _couponsApiState: MutableLiveData<GenericApiResultState> =
        MutableLiveData()
    val couponsApiState
        get() = _couponsApiState
    var couponList = mutableListOf<Any>()

    private var prodIdsInCart = mutableListOf<Int>()
    var cartTotal: Double = 0.0
    val grandTotal = MutableLiveData<String>()
    val stockQuantityResultState: MutableLiveData<GenericApiResultState> =
        MutableLiveData()
    var stockInfoList = mutableListOf<StockInfo>()

    val isLoggedIn by lazy {Prefs.getBoolean(Constant.PrefKeys.IS_LOGGED_IN)
    }

    private fun setResult(result: GenericApiResultState) =
        _cartState.postValue(result)

    fun updateGrandTotal(value: String) {
        grandTotalObservableField.set(value)
    }

    private fun updateProductQuantity(productCode: String, qty: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            cartListUseCase.updateQuantityUseCase(productCode = productCode, qty = qty)
        }

    @ExperimentalCoroutinesApi
    fun fetchAllCartItems(forceToShowPromoPrices: Boolean = false) =
        viewModelScope.launch(Dispatchers.IO) {
            cartListUseCase.getAllItemsUseCase(forceToShowPromoPrices)
                .onStart { setResult(GenericApiResultState.Loading) }
                .flowOn(Dispatchers.Default)
                .collect {
                    cartList = it.toMutableList()
                    _liveDataCartList.postValue(it)
                    if (cartList.isEmpty()) {
                        _cartErrorsMutableLiveData.postValue(Constants.CART_EMPTY_TO_PROCEED)
                        setResult(GenericApiResultState.Empty)
                    } else {
                        setResult(GenericApiResultState.Success(true))
                    }
                    calculateGrandTotal()
                    calculateDiscount()
                }
        }

    private fun calculateDiscount() {
        val discount = cartList.sumOf {
            it.savings * it.quantity
        }
        discountObservableField.set(
            com.lonwulf.core.utils.Utils.thousandFormatter(
                "$discount",
                alwaysShowDecimals = false
            )
        )
    }


    fun checkCartErrors() {
        grandTotalObservableField.get()?.replace(",", "")?.toDoubleOrNull() ?: 0.0
        when {
            isCartQuantityEmpty() -> {
                _cartErrorsMutableLiveData.setValue(Constants.EMPTY_QTY_ERROR)
            }

            cartList.isEmpty() -> _cartErrorsMutableLiveData.value = Constants.CART_EMPTY_TO_PROCEED

            else -> {
                _cartErrorsMutableLiveData.value = Constants.QTY_OK

            }
        }
    }

    private fun isCartQuantityEmpty() =
        TextUtils.isEmpty(_quantityObservableField.get().toString())

    @ExperimentalCoroutinesApi
    fun deleteCartItemFromDB(code: String) = viewModelScope.launch(Dispatchers.IO) {
        cartListUseCase.deleteAnItemUseCase(code)
            .onStart { }
            .flowOn(Dispatchers.Default)
            .collect {
                if (it == 1) {
                    // Fix for CM-3854
                    if (cartList.getOrNull(editedPosition) != null) {
                        cartList.removeAt(editedPosition)
                        calculateGrandTotal()
                        _liveDataCartList.postValue(cartList)
                        if (cartList.isEmpty()) {
                            _cartErrorsMutableLiveData.postValue(Constants.CART_EMPTY_TO_PROCEED)
                            setResult(GenericApiResultState.Empty)
                        }
                    }
                } else {
                    setResult(GenericApiResultState.Error(it.toString()))
                }
            }
    }


    fun getSubtotal(product: Product): String {
        return thousandFormatter(
            (product.price.times(product.quantity).toString())
        )
    }

    fun editQuantity(
        position: Int,
        product:Product,
        quantity: Int,
    ) {
        editedPosition = position
        product.quantity = quantity
        updateProductQuantity(product.code, quantity)
        calculateGrandTotal()
    }

    fun deleteCartItemAtPosition(position: Int, product: Product) {
        editedPosition = position
        productAtPosition = product
        deleteCartItemFromDB(productAtPosition?.code.toString())
    }

    private fun calculateGrandTotal() {
        if (cartList.isEmpty()) {
            grandTotalObservableField.set("0")
        } else {
            grandTotalObservableField.set(getGrandTotal().toString())
        }
        grandTotal.postValue(grandTotalObservableField.get())
        val cartCount: Int = if (cartList.contains(deliveryFeeProduct()) || cartList.contains(
                homeDeliveryProduct(getHDLProduct())
            )
        ) {
            cartList.size.minus(1)
        } else {
            cartList.size
        }
        if (cartCount > 1) {
            cartCountObservableField.set("$cartCount items")
        } else {
            cartCountObservableField.set("$cartCount item")
        }
    }

    fun thousandFormatter(total: String?, showCurrency: Boolean = false): String {
        return Utils.thousandFormatter(total, showCurrency)
    }

    fun confirmOrder(payNowChecked: Boolean, amount: Double, cart_list: List<Product>?) {
        viewModelScope.launch(Dispatchers.IO) {
            cartListUseCase.confirmOrderUseCase(
                getOrderSms(payNowChecked, amount, cart_list ?: cartList),
                (amount.minus(Prefs.getDouble(Constant.CartUtilConstants.COUPON_DISCOUNT)))
            )
                .onStart { setPlaceOrderResult(GenericApiResultState.Loading) }
                .flowOn(dispatcherProvider.default)
                .collect { result ->
                    if (result?.status == 200) {
                        setPlaceOrderResult(GenericApiResultState.Success(
                                true
                            )
                        )
                    } else {
                        setPlaceOrderResult(GenericApiResultState.Error(
                                result?.message ?: "Something went wrong"
                            )
                        )
                    }
                }
        }
    }

    private fun getOrderSms(
        payNowChecked: Boolean,
        amount: Double,
        cartList: List<com.lonwulf.core.domain.models.Product>
    ): String {
        return com.lonwulf.cart.util.CheckoutUtil.Companion.createOrderingSms(
            payNowChecked,
            amount.toInt(),
            cartList,
            Constant.Variants.API_CALL
        )
    }

    private fun setPlaceOrderResult(result: GenericApiResultState) =
        _placeOrderState.postValue(result)

    fun setPlaceOrderSummary() {
        calculateGrandTotal()
        grandTotalObservableField.set(
            Utils.thousandFormatter(
                "${getGrandTotal()}",
                alwaysShowDecimals = false
            )
        )
        val couponDiscount = Prefs.getDouble(Constant.CartUtilConstants.COUPON_DISCOUNT)
        val couponDiscountText = Prefs.getString(Constant.MultiCompanyKeys.COUNTRY_CURRENCY_NAME)
            .plus(" ")
            .plus(
                Utils.thousandFormatter(
                    total = couponDiscount.toString(),
                    alwaysShowDecimals = false
                )
            )
        savingsTotalObservable.set(
            if (couponDiscount > 0.0) {
                couponDiscountText
            } else {
                Prefs.getString(Constant.MultiCompanyKeys.COUNTRY_CURRENCY_NAME
                ).plus(" ")
                    .plus(discountObservableField.get())
            }
        )
    }

    fun clearCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            cartListUseCase.deleteMultipleItemsUseCase.invoke()
        }
    }

    fun setViewsObservableField(totalAmount: String, totalCount: String) {
        totalObservableField.set(totalAmount)
    }


    fun getLocalCurrency(): String =Prefs.getString(Constant.MultiCompanyKeys.COUNTRY_CURRENCY_NAME)
            ?: ""

    fun setHowToPayList(isLpp: Boolean = false) {
        var paymentList = getHowToPayList().asReversed()
        if (isLpp) {
            paymentList = getLppInstructionList().asReversed()
        }
    }

    private fun getHowToPayList(): ArrayList<PaymentInstruction> {
        val gson = com.google.gson.Gson()
        val paymentListString =Prefs.getString(Constant.PrefKeys.HOW_TO_PAY_LIST)
        val type = object : com.google.gson.reflect.TypeToken<ArrayList<PaymentInstruction>>() {}.type
        return if (paymentListString.isNullOrEmpty()) {
            ArrayList()
        } else {
            gson.fromJson(paymentListString, type)
        }
    }

    fun capitalize(title: String): String = title.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(
                Locale.getDefault()
            )
        } else {
            it.toString()
        }
    }

    fun getGrandTotal(): Double {
        return (CartUtil.getCartPayableTotals(cartList)).toDouble()
            .minus(Prefs.getDouble(Constant.CartUtilConstants.COUPON_DISCOUNT))
    }


    fun validateProductQuantity(productIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            cartListUseCase.validateQuantityUseCase(productIds = productIds)
                .onStart { }
                .flowOn(dispatcherProvider.ui)
                .collect { result ->
                    if (result.success) {
                        result.results?.let { validateQuantityDto ->
                            stockInfoList =
                                com.lonwulf.cart.data.mapper.mapQtyDtoToStockInfoList(
                                    validateQuantityDto
                                ).toMutableList()
                            setStockQtyApiResult(
                                GenericApiResultState.Success(
                                    true,
                                    result.results
                                )
                            )
                        } ?: run {
                            setStockQtyApiResult(
                                GenericApiResultState.Success(
                                    false,
                                    null
                                )
                            )
                        }
                    } else {
                        setStockQtyApiResult(
                            GenericApiResultState.Error(
                                result.message
                            )
                        )
                    }
                }
        }
    }

    fun removeOOSProductsFromCartList(outOfStockList: List<Product>) {
        outOfStockList.forEach { product ->
            deleteCartItemFromDB(code = product.code)
        }
    }

}