package com.lonwulf.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import com.lonwulf.navigation.DestinationConstants.CATEGORY_SCREEN
import com.lonwulf.navigation.DestinationConstants.HOME_SCREEN
import com.lonwulf.navigation.DestinationConstants.NOTIFICATIONS_SCREEN
import com.lonwulf.navigation.DestinationConstants.PROFILE_SCREEN


object DestinationConstants {
    const val HOME_SCREEN = "Home"
    const val NOTIFICATIONS_SCREEN = "Notifications"
    const val CATEGORY_SCREEN = "Categories"
    const val PRODUCT_DETAIL_SCREEN = "Product Details"
    const val PRODUCT_SCREEN = "Products screen"

    const val ORDER_SUMMARY_SCREEN = "ORDER_SUMMARY_SCREEN"
    const val ORDER_HISTORY_SCREEN = "_ORDER_HISTORY_SCREEN"
    const val CHECKOUT_SCREEN = "CHECKOUT_SCREEN"
    const val CART_SCREEN = "cart_screen"

    const val PROFILE_SCREEN = "profile_screen"
    const val LOGIN_SCREEN = "login_screen"
}

sealed class TopLevelDestinations(val route: String, val icon: ImageVector, title: String) {
    object HomeScreen : TopLevelDestinations(HOME_SCREEN, Icons.Outlined.Home, "Home")
    object CategoriesScreen :
        TopLevelDestinations(CATEGORY_SCREEN, Icons.Outlined.Menu, "Categories")

    object NotificationsScreen :
        TopLevelDestinations(NOTIFICATIONS_SCREEN, Icons.Outlined.Email, "Notifications")

    object ProfileScreen :
        TopLevelDestinations(PROFILE_SCREEN, Icons.Outlined.AccountCircle, "Profile")
}

sealed class Destinations(val route: String) {
    object LoginScreen : Destinations(DestinationConstants.LOGIN_SCREEN)
    object ProductScreen : Destinations(DestinationConstants.PRODUCT_SCREEN)
    object ProductDetailScreen : Destinations(DestinationConstants.PRODUCT_DETAIL_SCREEN)
    object CartScreen : Destinations(DestinationConstants.CART_SCREEN)
    object OrderSummaryScreen : Destinations(DestinationConstants.ORDER_SUMMARY_SCREEN)
    object OrderHistoryScreen : Destinations(DestinationConstants.ORDER_HISTORY_SCREEN)
    object CheckoutScreen : Destinations(DestinationConstants.CHECKOUT_SCREEN)


}
