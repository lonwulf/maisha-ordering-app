package com.lonwulf.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lonwulf.navigation.ui.SharedViewModel

interface NavComposable {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Composable(
        navHostController: NavHostController,
        sheetState: SheetState,
        snackbarHostState: SnackbarHostState,
        sharedViewModel: SharedViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navHostController: NavHostController,
    composables: Map<String, NavComposable>,
    sheetState: SheetState,
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel = hiltViewModel<SharedViewModel>(),
) {
//    val isDeviceSet by rememberUpdatedState(Prefs.getBoolean(PrefKeys.IS_DEVICE_SET))
//
    val destination = when {
        isSessionExpired -> Destinations.LoginScreen.route
        else -> TopLevelDestinations.HomeScreen.route

    }
//
//    LaunchedEffect(isSessionExpired, isDeviceSet) {
//        if (navHostController.currentDestination?.route != destination) {
//            navHostController.navigate(destination) {
//                popUpTo(navHostController.graph.route!!) { inclusive = true }
//            }
//        }
//    }
    NavHost(
        navController = navHostController, startDestination = destination
    ) {
        composables.forEach { (route, composable) ->
            composable(route = route) { entry ->
                composable.Composable(
                    navHostController, sheetState, snackbarHostState, sharedViewModel
                )
            }
        }
    }
}
