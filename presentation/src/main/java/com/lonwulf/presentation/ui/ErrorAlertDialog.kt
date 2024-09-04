package com.lonwulf.presentation.icons.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.lonwulf.presentation.R

@Composable
fun ErrorAlertDialog(
    onDismissRequest: () -> Unit, onConfirmation: () -> Unit, errMsg: String
) {
    AlertDialog(icon = {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "",
            tint = colorResource(R.color.primary_red)
        )
    },
        title = {
            Text(text = stringResource(R.string.error))
        },
        text = {
            Text(text = errMsg)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmation()
            }) {
                Text(stringResource(R.string.retry), color = colorResource(R.color.primary_blue))
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
        dismissButton = {
            TextButton(onClick = {
                onDismissRequest()
            }) {
                Text(stringResource(R.string.dismiss))
            }
        })
}