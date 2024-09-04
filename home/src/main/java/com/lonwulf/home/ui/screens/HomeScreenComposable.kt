package com.lonwulf.home.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.lonwulf.navigation.NavComposable
import com.lonwulf.navigation.ui.SharedViewModel

class HomeScreenComposable : NavComposable {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Composable(
        navHostController: NavHostController,
        sheetState: SheetState,
        snackbarHostState: SnackbarHostState,
        sharedViewModel: SharedViewModel
    ) {
        HomeScreen(navHostController = navHostController, sheetState = sheetState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController, sheetState: SheetState, modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {

        Text(text = stringResource(com.github.hussainderry.securepreferences.R.string.app_name))

    }
}