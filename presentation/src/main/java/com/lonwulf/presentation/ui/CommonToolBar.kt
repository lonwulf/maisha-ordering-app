package com.lonwulf.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.lonwulf.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonToolBar(onclick: () -> Unit, title: String) {
    TopAppBar(title = {
        Text(
            text = title,
            color = Color.White,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily
        )
    }, navigationIcon = {
        IconButton(onClick = { onclick() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
    }, colors = TopAppBarDefaults.largeTopAppBarColors(
        containerColor = colorResource(R.color.primary_blue)
    )
    )
}