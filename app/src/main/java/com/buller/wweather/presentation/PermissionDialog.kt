package com.buller.wweather.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.buller.wweather.R

@Composable
fun PermissionDialog(
    onDismiss: () -> Unit,
    onOkClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.need_permission)) },
        text = { Text(text = stringResource(R.string.need_permission_text)) },
        confirmButton = {
            Button(onClick = onOkClick) {
                Text(text = stringResource(R.string.OK))
            }
        }
    )
}