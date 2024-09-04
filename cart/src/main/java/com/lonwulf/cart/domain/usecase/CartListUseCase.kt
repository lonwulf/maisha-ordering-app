package com.lonwulf.cart.domain.usecase

@SuppressWarnings("LongParameterList")
class CartListUseCase(
    val cartItemCountUseCase: CartItemCountUseCase,
    val deleteAnItemUseCase: DeleteAnItemUseCase,
    val deleteMultipleItemsUseCase: DeleteMultipleItemsUseCase,
    val getAllItemsUseCase: GetAllItemsUseCase,
    val insertToCartUseCase: InsertToCartUseCase,
    val updateQuantityUseCase: UpdateQuantityUseCase,
    val deleteNonJncUseCase: DeleteNonJncProductsUseCase,
    val couponUseCase: ValidateCouponUseCase,
    val confirmOrderUseCase: ConfirmOrderUseCase,
    val validateQuantityUseCase: ValidateQuantityUseCase
)
