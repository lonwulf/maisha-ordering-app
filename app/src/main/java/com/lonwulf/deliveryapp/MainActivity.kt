package com.lonwulf.deliveryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lonwulf.deliveryapp.ui.theme.MaishaDeliveryTheme
import com.lonwulf.home.ui.screens.HomeScreenComposable
import com.lonwulf.navigation.Destinations
import com.lonwulf.navigation.NavGraph
import com.lonwulf.navigation.TopLevelDestinations
import com.lonwulf.navigation.ui.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaishaDeliveryTheme {
                val sheetState = rememberModalBottomSheetState()
                var showSnackBar = remember { mutableStateOf(false) }
                val sharedViewModel: SharedViewModel by viewModel<SharedViewModel>()
                val deviceNumber by sharedViewModel.deviceModelStateflow.collectAsState()
                val navController = rememberNavController()
                val navStackBackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navStackBackEntry?.destination
                val snackBarHostState = remember { SnackbarHostState() }
                val hideAppBarInScreens = listOf(
                    Destinations.LoginScreen.route,
                    Destinations.ProductScreen.route,
                    Destinations.ProductDetailScreen.route,
                    Destinations.CartScreen.route,
                    Destinations.OrderSummaryScreen.route,
                    Destinations.CheckoutScreen.route
                )
                val showBottomBar = currentDestination?.route !in hideAppBarInScreens

                Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }, topBar = {
                    Column {
                        if (showBottomBar) {
                            ToolBar(
                                sheetState,
                                scope = rememberCoroutineScope(),
                                navHostController = navController,
                                deviceNumber = deviceNumber.deviceName
                            )
                        }
//                        if (isConnected.not()) {
//                            ConnectivityStrip()
//                        }
                    }
                }, bottomBar = {
                    if (showBottomBar) {
                        BottomNavigationBar(
                            navHostController = navController,
                            currentDestination = currentDestination
                        )
                    }
                }, modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val composables = mapOf(
                            Destinations.LoginScreen.route to LoginScreenComposable(),
                            Destinations.CartScreen.route to CartScreenComposable(),
                            TopLevelDestinations.HomeScreen.route to HomeScreenComposable(),
                            TopLevelDestinations.CategoriesScreen.route to CategoriesScreenComposable(),
                            TopLevelDestinations.NotificationsScreen.route to NotificationsScreenComposable(),
                            TopLevelDestinations.ProfileScreen.route to ProfileScreenComposable(),
                            Destinations.ProductScreen.route to ProductScreenComposable(),
                            Destinations.ProductDetailScreen.route to ProductDetailScreenComposable(),
                            Destinations.CheckoutScreen.route to CheckoutScreenComposable(),
                            Destinations.OrderSummaryScreen.route to OrderSummaryScreenComposable(),
                            Destinations.OrderHistoryScreen.route to OrderHistoryScreenComposable()
                        )
                        NavGraph(
                            navHostController = navController,
                            composables = composables,
                            sheetState,
                            snackBarHostState,
                            sharedViewModel = sharedViewModel
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBar(
    sheetState: SheetState,
    scope: CoroutineScope,
    navHostController: NavHostController,
    deviceNumber: String
) {
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(colors = TopAppBarColors(
        containerColor = colorResource(R.color.primary_blue),
        scrolledContainerColor = colorResource(R.color.very_light_blue),
        navigationIconContentColor = colorResource(R.color.white),
        titleContentColor = colorResource(R.color.white),
        actionIconContentColor = colorResource(R.color.white)
    ), title = {
        Text(
            text = deviceNumber,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily
        )
    }, navigationIcon = {
        IconButton(onClick = {
            scope.launch {
                if (sheetState.isVisible) {
                    sheetState.hide()
                } else {
                    sheetState.expand()
                }
            }
        }) {
            Icon(
                imageVector = Icons.Filled.AccountCircle, contentDescription = ""
            )
        }
    }, actions = {
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "")
        }
        DropdownMenu(
            expanded = expanded, onDismissRequest = {}, modifier = Modifier.wrapContentSize()
        ) {
            DropdownMenuItem(text = { Text(text = stringResource(R.string.my_profile)) },
                onClick = {
                    navHostController.navigate(Destinations.ProfileScreen.route)
                    expanded = false
                })
            DropdownMenuItem(text = { Text(text = stringResource(R.string.about_us)) },
                onClick = {})

        }
    })
}

@Composable
fun BottomNavigationBar(
    navHostController: NavHostController, currentDestination: NavDestination?
) {
    val screens = listOf(
        TopLevelDestinations.HomeScreen,
        TopLevelDestinations.CategoriesScreen,
        TopLevelDestinations.NotificationsScreen,
        TopLevelDestinations.ProfileScreen
    )
    NavigationBar(containerColor = colorResource(R.color.bottom_nav_bar_color)) {
        screens.forEach { screen ->
            AddItem(screen, currentDestination, navHostController)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: TopLevelDestinations,
    currentDestination: NavDestination?,
    navHostController: NavHostController
) {
    NavigationBarItem(selected = currentDestination?.route == screen.route,
        label = { Text(text = screen.route) },
        colors = NavigationBarItemColors(
            selectedIconColor = colorResource(R.color.primary_blue),
            selectedTextColor = colorResource(R.color.primary_blue),
            unselectedIconColor = Color.Gray,
            unselectedTextColor = Color.Gray,
            selectedIndicatorColor = colorResource(R.color.very_light_blue),
            disabledIconColor = Color.Gray,
            disabledTextColor = Color.Gray
        ),
        icon = {
            BadgedBox(badge = {}) {
                Icon(
                    imageVector = screen.icon, contentDescription = null
                )
            }
        },
        onClick = {
            navHostController.navigate(screen.route) {
                popUpTo(navHostController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        })
}