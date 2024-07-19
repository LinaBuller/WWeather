package com.buller.wweather.presentation.settings.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

data class DialogInfo(val title: String, val confirmText: String, val cancelText: String)

@Composable
fun SimpleDialog(
    dialogInfo: DialogInfo,
    open: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (!open) return
    AlertDialog(
        title = {
            Text(dialogInfo.title)
        },
        text = {
            content()
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = dialogInfo.confirmText, style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dialogInfo.cancelText, style = MaterialTheme.typography.labelLarge)
            }
        }
    )
}

@Preview
@Composable
fun SimpleDialogPreview() = Surface {
    val dialogInfo = DialogInfo("Hello!", "OK", "Cancel")
    SimpleDialog(
        dialogInfo = dialogInfo,
        open = true,
        onConfirm = {},
        onDismiss = {}) {
        Text("It's my text in this dialog")
    }
}