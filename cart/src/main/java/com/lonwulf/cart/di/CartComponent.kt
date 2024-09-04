package com.lonwulf.cart.di

import com.lonwulf.core.di.CoreComponent
import com.lonwulf.core.di.scope.FeatureScope
import com.lonwulf.cart.presentation.CartActivity
import com.lonwulf.cart.presentation.screen.CartFragment
import com.lonwulf.cart.presentation.screen.CheckoutFragment
import com.lonwulf.cart.presentation.screen.DeliveryPointFragment
import com.lonwulf.cart.presentation.screen.JncCustomerCheckout
import com.lonwulf.cart.presentation.screen.LoanDetailsFragment
import com.lonwulf.cart.presentation.screen.LoanOrderSuccessDialog
import com.lonwulf.cart.presentation.screen.NonJistockOrdersFragment
import com.lonwulf.cart.presentation.screen.OrderSummaryFragment
import com.lonwulf.cart.presentation.screen.OutOfStockBottomSheetFragment
import com.lonwulf.cart.presentation.screen.PaymentsFragment
import com.lonwulf.cart.presentation.screen.PreviousRABottomSheetFragment
import com.lonwulf.cart.presentation.screen.RaMapFragment
import com.lonwulf.cart.presentation.screen.RaSearchResultsFragment
import com.lonwulf.cart.presentation.screen.ReturningCustomerFragment
import com.lonwulf.cart.presentation.screen.SelectedLoanDialogFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FeatureScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [
        CartModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ]
)
interface CartComponent {
    @ExperimentalCoroutinesApi
    @FlowPreview
//    fun inject(cartFragment: CartFragment)
//    fun inject(orderSummaryFragment: OrderSummaryFragment)
//    fun inject(paymentsFragment: PaymentsFragment)
//    fun inject(checkoutFragment: CheckoutFragment)
//
//    fun inject(outOfStockBottomSheetFragment: OutOfStockBottomSheetFragment)

}
