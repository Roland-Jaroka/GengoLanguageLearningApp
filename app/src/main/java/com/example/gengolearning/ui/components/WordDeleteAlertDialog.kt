package com.example.gengolearning.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun DeleteWordAlertDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        containerColor = MaterialTheme.colorScheme.background,
        icon = {
            Image(
                painter = painterResource(R.drawable.delete_icon2),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
            )
        },
        title = {
            Text(
                text = stringResource(R.string.delete_word_dialog_title),
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.delete_word_dialog_description)
            )
        },
        confirmButton = {
            MyAppButton(
                onClick = {
                    onConfirm()
                },
                text = stringResource(R.string.delete_word_dialog_button),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red
                )
            )
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                },
                text = stringResource(R.string.delete_word_dialog_cancel)
            )
        },
    )
}

@Preview
@Composable
private fun Preview() {
    DeleteWordAlertDialog(
        onConfirm = {},
        onDismiss = {}
    )
}